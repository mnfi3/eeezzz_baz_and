package ir.easybazi.api_service.address

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import ir.easybazi.api_service.ApiRequest
import ir.easybazi.api_service.Urls
import ir.easybazi.model.Address
import ir.easybazi.model.City
import ir.easybazi.model.State

class AddressService(private val context: Context) {


    fun getStates(onStatesReceived: onStatesReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.ADDRESS_STATES, JSONObject(), false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onStatesReceived.onComplete(status, message, ArrayList<State>())
                    return
                }

                val states = ArrayList<State>()
                try {
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val state: State
                        state = State.Parser.parse(data.getJSONObject(i))
                        states.add(state)
                    }

                    onStatesReceived.onComplete(status, message, states)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getStateCities(state_id: Int, onStateCitiesReceived: onStateCitiesReceived) {
        var url = Urls.ADDRESS_STATE_CITIES
        val id = Integer.toString(state_id)
        url = url.replace("{id}", id)

        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, JSONObject(), false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onStateCitiesReceived.onComplete(status, message, ArrayList<City>())
                    return
                }

                val cities = ArrayList<City>()
                try {
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val city: City
                        city = City.Parser.parse(data.getJSONObject(i))
                        cities.add(city)
                    }

                    onStateCitiesReceived.onComplete(status, message, cities)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    fun saveAddress(jsonObject: JSONObject, onAddressSaved: onAddressSaved) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.ADDRESS_INDEX, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onAddressSaved.onComplete(status, message, Address())
                    return
                }

                var address = Address()
                try {
                    address = Address.Parser.parse(response!!.getJSONObject("data"))
                    onAddressSaved.onComplete(status, message, address)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onStatesReceived {
        fun onComplete(status: Int, message: String, states: ArrayList<State>)
    }


    interface onStateCitiesReceived {
        fun onComplete(status: Int, message: String, cities: ArrayList<City>)
    }


    interface onAddressSaved {
        fun onComplete(status: Int, message: String, address: Address)
    }


}
