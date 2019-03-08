package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class OsmSearchedPlace {
    var place_id: Int = 0
    var osm_type: String = ""
    var osm_id: Int = 0
    var boundingbox: ArrayList<Double> = ArrayList()
    var lat: Double = 0.0
    var lon: Double = 0.0
    var display_name: String = ""
    var `class`: String = ""
    var type: String = ""
    var address: Address = Address()

    object Parser{
        fun parse(json: JSONObject): OsmSearchedPlace{
            val osm = OsmSearchedPlace()
            osm.place_id = json.getInt("place_id")
            osm.osm_type = json.getString("osm_type")
            osm.osm_id = json.getInt("osm_id")
            val boxArray = json.getJSONArray("boundingbox")
            for (i in 0..boxArray.length()-1){
                osm.boundingbox.add(boxArray.getDouble(i))
            }
            osm.lat = json.getDouble("lat")
            osm.lon = json.getDouble("lon")
            osm.display_name = json.getString("display_name")
            osm.`class` = json.getString("class")
            osm.type = json.getString("type")
//            osm.address = Address.Parser.parse(json.getJSONObject("address"))
            return osm
        }
    }

    class Address{
        var building: String = ""
        var road: String = ""
        var neighbourhood: String = ""
        var suburb: String = ""
        var city: String = ""
        var county: String = ""
        var state: String = ""
        var postcode: String = ""
        var country: String = ""

         object Parser{
            fun parse(json: JSONObject): Address{
                val address = Address()
                try {
                    address.building = json.getString("building")
                    address.road = json.getString("road")
                    address.neighbourhood = json.getString("neighbourhood")
                    address.suburb = json.getString("suburb")
                    address.city = json.getString("city")
                    address.county = json.getString("county")
                    address.state = json.getString("state")
                    address.postcode = json.getString("postcode")
                    address.country = json.getString("country")
                }catch (e: JSONException){

                }

                return address
            }
        }

    }
}