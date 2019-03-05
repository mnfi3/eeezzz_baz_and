package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class Device {
    var id: Int = 0
    var user_id: Int = 0
    var device_type: String? = null
    var client_key: String? = null
    var fcm_token: String? = null
    var created_at: String? = null


    object Parser {
        fun parse(`object`: JSONObject): Device {
            val device = Device()
            try {
                device.id = `object`.getInt("id")
                device.user_id = `object`.getInt("user_id")
                device.device_type = `object`.getString("device_type")
                device.fcm_token = `object`.getString("client_key")
                device.device_type = `object`.getString("fcm_token")
                device.created_at = `object`.getString("created_at")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return device
        }
    }
}
