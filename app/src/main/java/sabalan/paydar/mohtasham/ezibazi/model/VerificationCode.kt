package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class VerificationCode {
    var id: Int = 0
    var mobile: String? = null
    var code: String? = null
    var token: String? = null

    object Parser {
        fun parse(`object`: JSONObject): VerificationCode {
            val vc = VerificationCode()
            try {
                vc.id = `object`.getInt("id")
                vc.mobile = `object`.getString("mobile")
                vc.code = `object`.getString("code")
                vc.token = `object`.getString("token")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return vc
        }
    }
}