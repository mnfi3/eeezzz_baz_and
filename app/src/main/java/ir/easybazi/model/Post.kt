package ir.easybazi.model

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class Post : Serializable {

    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var image_url: String? = null
    var created_at: String? = null

    class Parser : Serializable {
        companion object {
            fun parse(postObj: JSONObject): Post {
                val post = Post()
                try {
                    post.id = postObj.getInt("id")
                    post.title = postObj.getString("title")
                    post.content = postObj.getString("content")
                    post.created_at = postObj.getString("created_at")
                    val photoArray = postObj.getJSONArray("photos")
                    for (i in 0 until photoArray.length()) {
                        val photo: Photo
                        photo = Photo.Parser.parse(photoArray.getJSONObject(i))
                        if (photo.type == "app_cover" || photo.type == "app_post_cover") {
                            post.image_url = photo.url
                            break
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                return post
            }
        }
    }
}
