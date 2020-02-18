package sabalan.paydar.mohtasham.ezibazi.api_service.account

import android.content.Context
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.VerificationCode

class VerificationCodeService (private val context: Context)
{

    fun requestRegisterCode(jsonObject: JSONObject, onSendCodeComplete: onSendCodeComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.VERIFICATION_CODE_REGISTER, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                onSendCodeComplete.onComplete(status, message)
            }
        })

    }

    fun requestPasswordRessetCode(jsonObject: JSONObject, onSendCodeComplete: onSendCodeComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.VERIFICATION_CODE_RESET_PASSWORD, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                onSendCodeComplete.onComplete(status, message)
            }
        })

    }

    fun verifyCode(jsonObject: JSONObject, onVerifyCodeComplete: onVerifyCodeComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.VERIFICATION_CODE_VERIFY, jsonObject, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error){
                    onVerifyCodeComplete.onComplete(status, message, VerificationCode())
                }else{
                    var vc = VerificationCode()
                    if(status == 1){
                        var json = response!!.getJSONObject("data")
                        json = json.getJSONObject("verification_code")
                        vc = VerificationCode.Parser.parse(json)
                    }
                    onVerifyCodeComplete.onComplete(status, message, vc)
                }
            }
        })

    }





    interface onSendCodeComplete {
        fun onComplete(status: Int, message: String)
    }



    interface onVerifyCodeComplete {
        fun onComplete(status: Int, message: String, vc: VerificationCode)
    }



}