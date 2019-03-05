package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

import sabalan.paydar.mohtasham.ezibazi.system.application.G

class RentType {
    var id: Int = 0
    var day_count: Int = 0
    var price_percent: Int = 0

    object Parser {
        fun parse(`object`: JSONObject): RentType {
            val rentType = RentType()
            try {
                rentType.id = `object`.getInt("id")
                rentType.day_count = `object`.getInt("day_count")
                rentType.price_percent = `object`.getInt("price_percent")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return rentType
        }

    }

    companion object {


        fun findById(id: Int): RentType {
            var rentType = RentType()
            for (i in G.rentTypes.indices) {
                if (id == G.rentTypes[i].id) {
                    rentType = G.rentTypes[i]
                    break
                }
            }
            return rentType
        }
    }
}
