package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class ShopRequest {
    var id: Int = 0
    var user_id: Int = 0
    var game_for_shop_id: Int = 0
    var address_id: Int = 0
    var game_price: Int = 0
    var is_sent: Int = 0
    var is_success: Int = 0
    var is_finish: Int = 0
    var created_at: String? = null
    var game: Game? = null


    object Parser {
        fun parse(`object`: JSONObject): ShopRequest {
            val request = ShopRequest()
            try {
                request.id = `object`.getInt("id")
                request.user_id = `object`.getInt("user_id")
                request.game_for_shop_id = `object`.getInt("game_for_shop_id")
                request.address_id = `object`.getInt("address_id")
                request.game_price = `object`.getInt("game_price")
                request.is_sent = `object`.getInt("is_sent")
                request.is_success = `object`.getInt("is_success")
                request.is_finish = `object`.getInt("is_finish")
                request.created_at = `object`.getString("created_at")
                request.game = Game.Parser.parse(`object`.getJSONObject("game_for_shop"))


            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return request
        }
    }

}
