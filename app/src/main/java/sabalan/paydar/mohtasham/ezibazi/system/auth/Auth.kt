package sabalan.paydar.mohtasham.ezibazi.system.auth

import sabalan.paydar.mohtasham.ezibazi.api_service.account.LoginCheckerService
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.UserPrefManager
import sabalan.paydar.mohtasham.ezibazi.model.User

object Auth {


    val userToken: String?
        get() {
            val user = user
            if (user.token == null) {
                user.token = ""
            }
            return user.token
        }


    private val user: User
        get() {
            val prefManager = UserPrefManager(G.context)
            val user = prefManager.user
            if (user.full_name == null || user.token == null) {
                user.full_name = ""
                user.token = ""
            }
            return user
        }

    fun loginCheck(onLoginCheck: G.onLoginCheck) {
        val user = Auth.user
        val service = LoginCheckerService(G.context)
        service.check(user, object : LoginCheckerService.onLoginCheckComplete {
            override fun onComplete(isLoggedIn: Boolean, full_name: String) {
                user.full_name = full_name
                onLoginCheck.onCheck(user, isLoggedIn)
                G.isLoggedIn = isLoggedIn
            }
        })
    }
}
