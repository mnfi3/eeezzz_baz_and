package sabalan.paydar.mohtasham.ezibazi.api_service.payment

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls

class RentPayService(private val context: Context) {


    fun rentWithWallet(jsonObject: JSONObject, onRentWithWalletComplete: onRentWithWalletComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.RENT_WITH_WALLET, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRentWithWalletComplete.onComplete(status, message)
                    return
                }
                onRentWithWalletComplete.onComplete(status, message)
            }
        })

    }


    fun rentWithBank(jsonObject: JSONObject, onRentWithBankComplete: onRentWithBankComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.RENT_WITH_BANK, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRentWithBankComplete.onComplete(status, message, "")
                    return
                }
                var url = ""
                try {
                    url = response!!.getString("data")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                onRentWithBankComplete.onComplete(status, message, url)
            }
        })

    }


    interface onRentWithWalletComplete {
        fun onComplete(status: Int, message: String)
    }

    interface onRentWithBankComplete {
        fun onComplete(status: Int, message: String, pay_url: String)
    }


}
