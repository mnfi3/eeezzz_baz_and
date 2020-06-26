package ir.easybazi.model


import org.json.JSONException
import org.json.JSONObject

class Video {
    var id: Int = 0
    var url: String? = null
    var type: String? = null

    object Parser {
        fun parse(jsonObject: JSONObject): Video {
            val video = Video()
            try {
                video.id = jsonObject.getInt("id")
                video.url = jsonObject.getString("url")
                video.type = jsonObject.getString("type")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return video
        }
    }


}
