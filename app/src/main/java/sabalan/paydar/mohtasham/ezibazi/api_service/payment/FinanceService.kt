package sabalan.paydar.mohtasham.ezibazi.api_service.payment

import android.content.Context

import org.json.JSONException
import org.json.JSONObject


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls

class FinanceService(private val context: Context) {


    fun getPayUrl(jsonObject: JSONObject, onCreditComplete: onCreditComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.INCREASE_CREDIT, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onCreditComplete.onComplete(status, message, "")
                    return
                }

                var url = ""
                try {
                    url = response!!.getString("data")
                    onCreditComplete.onComplete(status, message, url)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    interface onCreditComplete {
        fun onComplete(status: Int, message: String, url: String)
    }


}
