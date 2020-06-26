package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject

class Photo {

    var id: Int = 0
    var url: String? = null
    var type: String? = null
    var width: Int = 0
    var height: Int = 0

    object Parser {
        fun parse(jsonObject: JSONObject): Photo {
            val photo = Photo()
            try {
                photo.id = jsonObject.getInt("id")
                photo.url = jsonObject.getString("url")
                photo.type = jsonObject.getString("type")
                photo.width = jsonObject.getInt("width")
                photo.height = jsonObject.getInt("height")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return photo
        }
    }


}
