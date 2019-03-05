package sabalan.paydar.mohtasham.ezibazi.api_service.firebase

import android.content.Context


import org.json.JSONException
import org.json.JSONObject


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.Device

class FireBaseApiService(private val context: Context) {


    fun refreshFcmToken(`object`: JSONObject, onRefreshTokenReceived: onRefreshTokenReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.REFRESH_FCM_TOKEN, `object`, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                var status = status
                var message = message
                if (error) {
                    onRefreshTokenReceived.onReceived(status, message, Device())
                    return
                }

                val device: Device
                try {
                    status = response!!.getInt("status")
                    message = response.getString("message")
                    device = Device.Parser.parse(response.getJSONObject("data"))
                    onRefreshTokenReceived.onReceived(status, message, device)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    interface onRefreshTokenReceived {
        fun onReceived(status: Int, message: String, device: Device)
    }


}
