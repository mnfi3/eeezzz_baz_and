package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable

class MainSlider : Serializable {
    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var on_click: String? = null
    var image_url: String? = null


    class Parser : Serializable {
        companion object {
            @Throws(JSONException::class)
            fun parse(sliderObject: JSONObject): MainSlider {
                val slider = MainSlider()
                slider.id = sliderObject.getInt("id")
                slider.title = sliderObject.getString("title")
                slider.content = sliderObject.getString("content")
                slider.on_click = sliderObject.getString("on_click")
                val photo = sliderObject.getJSONArray("photos")
                slider.image_url = photo.getJSONObject(0).getString("url")

                return slider
            }
        }
    }
}
