package sabalan.paydar.mohtasham.ezibazi.api_service.main

import android.content.Context


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.Post

class PostService(private val context: Context) {


    fun getMainPosts(page_num: Int, onPostsReceived: onPostsReceived) {
        val url = Urls.POST_INDEX + "?page=" + page_num
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onPostsReceived.onReceived(status, message, ArrayList<Post>(), Paginate())
                    return
                }

                try {
                    val posts = ArrayList<Post>()
                    val jsonData = response!!.getJSONObject("data")
                    val paginate = Paginate.Parser.parse(jsonData)
                    val data = jsonData.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val postObj = data.getJSONObject(i)
                        posts.add(Post.Parser.parse(postObj))
                    }

                    onPostsReceived.onReceived(status, message, posts, paginate)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }

    fun getSearchedPosts(`object`: JSONObject, onSearchedPostsReceived: onSearchedPostsReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.POST_SEARCH, `object`, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSearchedPostsReceived.onReceived(status, message, ArrayList())
                    return
                }

                try {
                    val posts = ArrayList<Post>()
                    if (status == 1) {
                        val data = response!!.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            posts.add(Post.Parser.parse(data.getJSONObject(i)))
                        }
                    }

                    onSearchedPostsReceived.onReceived(status, message, posts)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    fun getRelatedPosts(game_id: Int, onRelatedPostsReceived: onRelatedPostsReceived) {
        val url = Urls.GAME_RELATED_POSTS + "/" + Integer.toString(game_id)
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRelatedPostsReceived.onReceived(status, message, ArrayList<Post>())
                    return
                }

                try {
                    val posts = ArrayList<Post>()
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        posts.add(Post.Parser.parse(data.getJSONObject(i)))
                    }

                    onRelatedPostsReceived.onReceived(status, message, posts)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onPostsReceived {
        fun onReceived(status: Int, message: String, posts: List<Post>, paginate: Paginate)
    }

    interface onSearchedPostsReceived {
        fun onReceived(status: Int, message: String, posts: List<Post>)
    }


    interface onRelatedPostsReceived {
        fun onReceived(status: Int, message: String, posts: List<Post>)
    }


}
