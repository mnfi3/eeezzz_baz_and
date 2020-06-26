package ir.easybazi.api_service.account

import android.content.Context
import org.json.JSONObject
import ir.easybazi.api_service.ApiRequest
import ir.easybazi.api_service.Urls
import ir.easybazi.model.Settlement

class SettlementService(private val context: Context)
{
    fun getLastSettlement(onLastSettlementReceived: onLastSettlementReceived){
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.LAST_SETTLEMENT, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                var settlement = Settlement()
                if (status == 0) {
                    onLastSettlementReceived.onComplete(status, message, settlement)
                }else{
                    var json = response!!.getJSONObject("data")
                    settlement = Settlement.Parser.parse(json)
                    onLastSettlementReceived.onComplete(status, message, settlement)
                }
            }
        })
    }






    fun requestSettlement(jsonObject: JSONObject, onSettlementRequestComplete: onSettlementRequestComplete){
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.SETTLEMENT_REQUEST, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                var settlement = Settlement()
                if (status == 0) {
                    onSettlementRequestComplete.onComplete(status, message, settlement)
                }else{
                    var json = response!!.getJSONObject("data")
                    settlement = Settlement.Parser.parse(json)
                    onSettlementRequestComplete.onComplete(status, message, settlement)
                }
            }
        })
    }


    interface onLastSettlementReceived {
        fun onComplete(status: Int, message: String, settlement: Settlement)
    }


    interface onSettlementRequestComplete {
        fun onComplete(status: Int, message: String, settlement: Settlement)
    }

}