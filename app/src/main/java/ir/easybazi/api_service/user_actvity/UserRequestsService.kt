package ir.easybazi.api_service.user_actvity

import android.content.Context


import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import ir.easybazi.api_service.ApiRequest
import ir.easybazi.api_service.Urls
import ir.easybazi.model.RentRequest
import ir.easybazi.model.ShopRequest

class UserRequestsService(private val context: Context) {


    fun getRentRequests(onRentRequestsReceived: onRentRequestsReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.USER_RENT_REQUESTS, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRentRequestsReceived.onReceived(status, message, ArrayList<RentRequest>())
                    return
                }

                try {
                    val requests = ArrayList<RentRequest>()
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val requestObj = data.getJSONObject(i)
                        requests.add(RentRequest.Parser.parse(requestObj))
                    }

                    onRentRequestsReceived.onReceived(status, message, requests)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getShopRequests(onShopRequestsReceived: onShopRequestsReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.USER_BUY_REQUESTS, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onShopRequestsReceived.onReceived(status, message, ArrayList<ShopRequest>())
                    return
                }

                try {
                    val requests = ArrayList<ShopRequest>()
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val requestObj = data.getJSONObject(i)
                        requests.add(ShopRequest.Parser.parse(requestObj))
                    }

                    onShopRequestsReceived.onReceived(status, message, requests)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    interface onRentRequestsReceived {
        fun onReceived(status: Int, message: String, requests: List<RentRequest>)
    }

    interface onShopRequestsReceived {
        fun onReceived(status: Int, message: String, requests: List<ShopRequest>)
    }


}
