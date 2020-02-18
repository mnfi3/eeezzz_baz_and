package sabalan.paydar.mohtasham.ezibazi.api_service.account

import android.content.Context
import org.json.JSONException
import org.json.JSONObject

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.User

class AccountService(private val context: Context) {


    fun login(jsonObject: JSONObject, onLoginComplete: onLoginComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.LOGIN, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onLoginComplete.onComplete(status, message, "", User())
                    return
                }
                var token = ""
                var user = User()

                try {
                    if (status == 1) {
                        token = response!!.getJSONObject("data").getString("token")
                        val userObj = response.getJSONObject("data").getJSONObject("user")
                        user = User.Parser.parse(userObj)
                        user.token = token
                    }

                    onLoginComplete.onComplete(status, message, token, user)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
        })

    }


    fun register(jsonObject: JSONObject, onRegisterComplete: onRegisterComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.REGISTER, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRegisterComplete.onComplete(status, message, "", User())
                    return
                }

                var token = ""
                var user = User()
                try {
                    token = response!!.getJSONObject("data").getString("token")
                    val userObj = response.getJSONObject("data").getJSONObject("user")
                    user = User.Parser.parse(userObj)

                    onRegisterComplete.onComplete(status, message, token, user)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    fun logout(onLogoutComplete: onLogoutComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.LOGOUT, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onLogoutComplete.onComplete(status, message)
                    return
                }
                onLogoutComplete.onComplete(status, message)
            }
        })
    }


    fun resetPassword(jsonObject: JSONObject, onRessetPasswordComplete: onResetPasswordComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.RESET_PASSWORD, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                onRessetPasswordComplete.onComplete(status, message)
            }
        })

    }




    interface onLoginComplete {
        fun onComplete(status: Int, message: String, token: String, user: User)
    }

    interface onLogoutComplete {
        fun onComplete(status: Int, message: String)
    }


    interface onRegisterComplete {
        fun onComplete(status: Int, message: String, token: String, user: User)
    }


    interface onResetPasswordComplete {
        fun onComplete(status: Int, message: String)
    }


}
