package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable

class User : Serializable {
    var id: Int = 0
    var full_name: String? = null
    var email: String? = null
    var password: String? = null
    var invite_code: String? = null
    var client_key: String? = null
    var token: String? = null


    class Parser : Serializable {
        companion object {
            @Throws(JSONException::class)
            fun parse(`object`: JSONObject): User {
                val user = User()
                user.id = `object`.getInt("id")
                user.full_name = `object`.getString("full_name")
                user.email = `object`.getString("email")
                user.invite_code = `object`.getString("invite_code")
                return user
            }
        }
    }
}
