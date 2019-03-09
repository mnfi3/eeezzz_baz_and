package sabalan.paydar.mohtasham.ezibazi.api_service.osm

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener


import org.json.JSONArray

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.OsmSearchedPlace

class OsmService(private val context: Context) {


    fun searchSelectedCity(limit:Int, state: String, city: String, search: String, onOsmSearchedPlaceReceived: onOsmPlacesReceived) {
        val url = Urls.OSM_SEARCH;
        AndroidNetworking.initialize(context)
        AndroidNetworking.get(url)
                .addPathParameter("language", "persian")
                .addPathParameter("country", "iran")
                .addPathParameter("state", state)
                .addPathParameter("city", city)
                .addPathParameter("street", search)
                .addPathParameter("query", "")
                .addPathParameter("limit", limit.toString())
                .addPathParameter("addressdetails", 1.toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        val jsonArray:JSONArray = response!!
                        val locations = ArrayList<OsmSearchedPlace>()
                        var location: OsmSearchedPlace
                        for (i in 0..jsonArray.length()-1){
                            location = OsmSearchedPlace.Parser.parse(response.getJSONObject(i))
                            locations.add(location)
                        }
                        onOsmSearchedPlaceReceived.onReceived(1, "", locations)
                    }

                    override fun onError(anError: ANError?) {
                        onOsmSearchedPlaceReceived.onReceived(0, "خطا در ارتباط با osm", ArrayList<OsmSearchedPlace>())
                    }
                })
    }



    fun search(limit:Int, state: String, city: String, search: String, onOsmSearchedPlaceReceived: onOsmPlacesReceived) {
        val url = Urls.OSM_SEARCH;
        AndroidNetworking.initialize(context)
        AndroidNetworking.cancel("osm_search")
        AndroidNetworking.get(url)
                .addPathParameter("language", "persian")
                .addPathParameter("country", "iran")
                .addPathParameter("state", state)
                .addPathParameter("city", city)
                .addPathParameter("street", search)
                .addPathParameter("query", "")
                .addPathParameter("limit", limit.toString())
                .addPathParameter("addressdetails", 1.toString())
                .setPriority(Priority.HIGH)
                .setTag("osm_search")
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        val jsonArray:JSONArray = response!!
                        val locations = ArrayList<OsmSearchedPlace>()
                        var location: OsmSearchedPlace
                        for (i in 0..jsonArray.length()-1){
                            location = OsmSearchedPlace.Parser.parse(response.getJSONObject(i))
                            locations.add(location)
                        }
                        onOsmSearchedPlaceReceived.onReceived(1, "", locations)
                    }

                    override fun onError(anError: ANError?) {
                        onOsmSearchedPlaceReceived.onReceived(0, "خطا در ارتباط با osm", ArrayList<OsmSearchedPlace>())
                    }
                })
    }



    interface onOsmPlacesReceived {
        fun onReceived(status: Int, message: String, places: List<OsmSearchedPlace>)
    }



}
