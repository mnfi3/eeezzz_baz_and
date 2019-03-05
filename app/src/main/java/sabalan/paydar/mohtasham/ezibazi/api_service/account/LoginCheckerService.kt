package sabalan.paydar.mohtasham.ezibazi.api_service.account

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.User


class LoginCheckerService(private val context: Context) {


    fun check(user: User, onLoginCheckComplete: onLoginCheckComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.LOGIN_CHECK, null!!, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onLoginCheckComplete.onComplete(false, "")
                    return
                }
                var isLoggedIn = false
                var full_name = ""
                try {
                    if (status == 1) {
                        isLoggedIn = true
                        full_name = response!!.getJSONObject("data").getString("full_name")
                    } else {
                        isLoggedIn = false
                    }

                    onLoginCheckComplete.onComplete(isLoggedIn, full_name)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    interface onLoginCheckComplete {
        fun onComplete(isLoggedIn: Boolean, full_name: String)
    }


}
