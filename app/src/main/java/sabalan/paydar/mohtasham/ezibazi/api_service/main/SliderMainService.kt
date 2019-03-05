package sabalan.paydar.mohtasham.ezibazi.api_service.main

import android.content.Context


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.MainSlider

class SliderMainService(private val context: Context) {


    fun getMainSlider(onSliderReceived: onSliderReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.MAIN_SLIDER, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSliderReceived.onReceived(message, ArrayList<MainSlider>())
                    return
                }

                val sliders = ArrayList<MainSlider>()
                try {
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val sliderObject = data.getJSONObject(i)
                        sliders.add(MainSlider.Parser.parse(sliderObject))
                    }

                    onSliderReceived.onReceived(message, sliders)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }

    interface onSliderReceived {
        fun onReceived(message: String, sliders: List<MainSlider>)
    }


}
