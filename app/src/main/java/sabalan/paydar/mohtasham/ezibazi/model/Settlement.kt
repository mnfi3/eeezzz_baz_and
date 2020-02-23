package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

class Settlement {

    var id: Int = 0
    var user_id: Int = 0
    var first_amount = 0
    var final_amount = 0
    var bank_card_number:String? = null
    var bank_account_number:String? = null
    var bank_shba_number:String? = null
    var bank_account_owner_name:String? = null
    var is_seen = 0
    var is_confirmed = 0
    var is_paid = 0
    var created_at:String? = null

    object Parser{
        fun parse(json: JSONObject): Settlement{
            val settlement = Settlement()
            try {
                settlement.id = json.getInt("id")
                settlement.user_id = json.getInt("user_id")
                settlement.first_amount = json.getInt("first_amount")
                settlement.final_amount = json.getInt("final_amount")
                settlement.bank_card_number = json.getString("bank_card_number")
                settlement.bank_account_number = json.getString("bank_account_number")
                settlement.bank_shba_number = json.getString("bank_shba_number")
                settlement.bank_account_owner_name = json.getString("bank_account_owner_name")
                settlement.is_seen = json.getInt("is_seen")
                settlement.is_confirmed = json.getInt("is_confirmed")
                settlement.is_paid = json.getInt("is_paid")
                settlement.created_at = json.getString("created_at")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return settlement
        }
    }

}