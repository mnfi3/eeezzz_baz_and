package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class Address {
    var id: Int = 0
    var user_id: Int = 0
    var state_id: Int = 0
    var city_id: Int = 0
    var postcode: String? = null
    var home_phone_number: String? = null
    var content: String? = null
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    var created_at: String? = null
    var state: State? = null
    var city: City? = null


    val addressText: String
        get() {
            var text = ""
            if (state != null)
                text += " استان : " + state!!.name + "\n"
            if (city != null)
                text += " شهر : " + city!!.name + "\n"
            text += " آدرس : $content\n"
            text += " کدپستی : $postcode\n"
            text += " شماره تلفن : $home_phone_number\n"
            return text
        }


    object Parser {
        fun parse(`object`: JSONObject): Address {
            val address = Address()
            try {
                if (`object`.getJSONObject("state") != null) {
                    address.state = State.Parser.parse(`object`.getJSONObject("state"))
                }
                if (`object`.getJSONObject("city") != null) {
                    address.city = City.Parser.parse(`object`.getJSONObject("city"))
                }
                address.id = `object`.getInt("id")
                address.user_id = `object`.getInt("user_id")
                address.state_id = `object`.getInt("state_id")
                address.city_id = `object`.getInt("city_id")
                address.postcode = `object`.getString("postcode")
                address.home_phone_number = `object`.getString("home_phone_number")
                address.content = `object`.getString("content")
                address.latitude = `object`.getDouble("latitude")
                address.longitude = `object`.getDouble("longitude")
                address.created_at = `object`.getString("created_at")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return address
        }
    }
}
