package ir.easybazi.api_service.account

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import ir.easybazi.api_service.ApiRequest
import ir.easybazi.api_service.Urls
import ir.easybazi.model.User


class LoginCheckerService(private val context: Context) {


    fun check(user: User, onLoginCheckComplete: onLoginCheckComplete) {
        val apiRequest = ApiRequest(context)

        apiRequest.request(ApiRequest.GET, Urls.LOGIN_CHECK, JSONObject(), true, object : ApiRequest.onDataReceived {
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
