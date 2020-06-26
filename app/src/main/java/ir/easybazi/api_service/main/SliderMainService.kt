package ir.easybazi.api_service.main

import android.content.Context


import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import ir.easybazi.api_service.ApiRequest
import ir.easybazi.api_service.Urls
import ir.easybazi.model.MainSlider

class SliderMainService(private val context: Context) {


    fun getMainSlider(onSliderReceived: onSliderReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.MAIN_SLIDER, JSONObject(), false, object : ApiRequest.onDataReceived {
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
