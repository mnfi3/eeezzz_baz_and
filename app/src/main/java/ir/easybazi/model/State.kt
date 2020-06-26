package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject

class State {
    var id: Int = 0
    var name: String? = null

    object Parser {

        fun parse(`object`: JSONObject): State {
            val state = State()
            try {
                state.id = `object`.getInt("id")
                state.name = `object`.getString("name")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return state
        }
    }
}
