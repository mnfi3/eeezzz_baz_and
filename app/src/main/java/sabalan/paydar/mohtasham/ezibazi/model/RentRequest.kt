package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class RentRequest {
    var id: Int = 0
    var user_id: Int = 0
    var game_for_rent_id: Int = 0
    var address_id: Int = 0
    var game_price: Int = 0
    var rent_price: Int = 0
    var is_sent: Int = 0
    var is_success: Int = 0
    var is_finish: Int = 0
    var finished_at: String? = null
    var created_at: String? = null
    var rent_day_count: Int = 0
    var game: Game? = null


    object Parser {
        fun parse(`object`: JSONObject): RentRequest {
            val request = RentRequest()
            try {
                request.id = `object`.getInt("id")
                request.user_id = `object`.getInt("user_id")
                request.game_for_rent_id = `object`.getInt("game_for_rent_id")
                request.address_id = `object`.getInt("address_id")
                request.game_price = `object`.getInt("game_price")
                request.rent_price = `object`.getInt("rent_price")
                request.is_sent = `object`.getInt("is_sent")
                request.is_success = `object`.getInt("is_success")
                request.is_finish = `object`.getInt("is_finish")
                request.finished_at = `object`.getString("finished_at")
                request.created_at = `object`.getString("created_at")
                request.rent_day_count = `object`.getJSONObject("rent_type").getInt("day_count")
                request.game = Game.Parser.parse(`object`.getJSONObject("game_for_rent"))


            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return request
        }
    }

}
