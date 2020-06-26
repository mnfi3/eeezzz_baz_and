package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject
import ir.easybazi.model.User

class Comment {
    var id: Int = 0
    var user_id: Int = 0
    var commentable_id: Int = 0
    var commentable_type: String? = null
    var text: String? = null
    var created_at: String? = null
    var user: User? = null

    object Parser {
        fun parse(jsonObject: JSONObject): Comment {
            val comment = Comment()
            try {
                val userObj = jsonObject.getJSONObject("user")
                comment.user = User.Parser.parse(userObj)
                comment.id = jsonObject.getInt("id")
                comment.user_id = jsonObject.getInt("user_id")
                comment.commentable_id = jsonObject.getInt("commentable_id")
                comment.commentable_type = jsonObject.getString("commentable_type")
                comment.text = jsonObject.getString("text")
                comment.created_at = jsonObject.getString("created_at")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return comment
        }
    }
}
