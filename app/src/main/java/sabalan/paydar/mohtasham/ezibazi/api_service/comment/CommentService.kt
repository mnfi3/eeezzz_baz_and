package sabalan.paydar.mohtasham.ezibazi.api_service.comment

import android.content.Context

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.Comment
import sabalan.paydar.mohtasham.ezibazi.model.Paginate

class CommentService(private val context: Context) {


    fun getComments(game_info_id: Int, page_num: Int, onCommentsReceived: onCommentsReceived) {
        val url = Urls.GAME_INFO_COMMENTS + "/" + Integer.toString(game_info_id) + "?page=" + Integer.toString(page_num)

        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, JSONObject(), false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onCommentsReceived.onReceived(status, message, ArrayList<Comment>(), Paginate())
                    return
                }

                val comments = ArrayList<Comment>()
                var paginate: Paginate? = null
                try {
                    val jsonData = response!!.getJSONObject("data")
                    paginate = Paginate.Parser.parse(jsonData)
                    val data = jsonData.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val commentObj = data.getJSONObject(i)
                        comments.add(Comment.Parser.parse(commentObj))
                    }

                    onCommentsReceived.onReceived(status, message, comments, paginate)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }

    fun addComment(`object`: JSONObject, onAddCommentReceived: onAddCommentReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.COMMENT_INDEX, `object`, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onAddCommentReceived.onReceived(status, message, Comment())
                    return
                }

                val comment: Comment
                try {
                    comment = Comment.Parser.parse(response!!.getJSONObject("data"))
                    onAddCommentReceived.onReceived(status, message, comment)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onCommentsReceived {
        fun onReceived(status: Int, message: String, comments: List<Comment>, paginate: Paginate?)
    }

    interface onAddCommentReceived {
        fun onReceived(status: Int, message: String, comment: Comment)
    }


}
