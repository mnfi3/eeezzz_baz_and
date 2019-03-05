package sabalan.paydar.mohtasham.ezibazi.api_service.payment

import android.content.Context


import org.json.JSONException
import org.json.JSONObject


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls

class ShopPayService(private val context: Context) {


    fun shopWithWallet(jsonObject: JSONObject, onShopWithWalletComplete: onShopWithWalletComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.SHOP_WITH_WALLET, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onShopWithWalletComplete.onComplete(status, message)
                    return
                }
                onShopWithWalletComplete.onComplete(status, message)
            }
        })
    }


    fun shopWithBank(jsonObject: JSONObject, onShopWithBankComplete: onShopWithBankComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.SHOP_WITH_BANK, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onShopWithBankComplete.onComplete(status, message, "")
                    return
                }

                var pay_url = ""
                try {
                    pay_url = response!!.getString("data")
                    onShopWithBankComplete.onComplete(status, message, pay_url)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onShopWithWalletComplete {
        fun onComplete(status: Int, message: String)
    }

    interface onShopWithBankComplete {
        fun onComplete(status: Int, message: String, pay_url: String)
    }


}
