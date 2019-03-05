package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class City {
    var id: Int = 0
    var state_id: Int = 0
    var name: String? = null

    object Parser {

        fun parse(`object`: JSONObject): City {
            val city = City()
            try {
                city.id = `object`.getInt("id")
                city.state_id = `object`.getInt("state_id")
                city.name = `object`.getString("name")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return city
        }
    }
}
