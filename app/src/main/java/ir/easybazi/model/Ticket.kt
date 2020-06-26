package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject

class Ticket {
    var id: Int = 0
    var is_user_sent: Int = 0
    var text: String? = null
    var is_seen: Int = 0
    var created_at: String? = null

    object Parser {
        @Throws(JSONException::class)
        fun parse(`object`: JSONObject): Ticket {
            val ticket = Ticket()
            ticket.id = `object`.getInt("id")
            ticket.is_user_sent = `object`.getInt("is_user_sent")
            ticket.text = `object`.getString("text")
            ticket.is_seen = `object`.getInt("is_seen")
            ticket.created_at = `object`.getString("created_at")
            return ticket
        }
    }


}
