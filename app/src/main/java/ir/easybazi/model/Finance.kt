package ir.easybazi.model


import org.json.JSONException
import org.json.JSONObject

class Finance {
    var user_id: Int = 0
    var bank_card_number: String? = null
    var bank_account_number: String? = null
    var bank_shba_number: String? = null
    var bank_account_owner_name: String? = null
    var user_balance: Int = 0


    object Parser {
        fun parse(`object`: JSONObject): Finance {
            val finance = Finance()
            try {
                finance.user_id = `object`.getInt("user_id")
                finance.bank_card_number = `object`.getString("bank_card_number")
                finance.bank_account_number = `object`.getString("bank_account_number")
                finance.bank_shba_number = `object`.getString("bank_shba_number")
                finance.bank_account_owner_name = `object`.getString("bank_account_owner_name")
                finance.user_balance = `object`.getInt("user_balance")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return finance
        }
    }


}
