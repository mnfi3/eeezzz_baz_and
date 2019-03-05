package sabalan.paydar.mohtasham.ezibazi.api_service.main

import android.content.Context


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.CityPrefManager
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.model.Paginate

class ShopService(private val context: Context) {


    fun getMainShops(page_num: Int, onShopsReceived: onShopsReceived) {
        val url = Urls.SHOP_INDEX0 + "/" + Integer.toString(CityPrefManager(context).cityId) + "?page=" + page_num
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onShopsReceived.onReceived(status, message, ArrayList<Game>(), Paginate())
                    return
                }

                try {
                    val games = ArrayList<Game>()
                    val jsonData = response!!.getJSONObject("data")
                    val paginate = Paginate.Parser.parse(jsonData)
                    val data = jsonData.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val gameObj = data.getJSONObject(i)
                        games.add(Game.Parser.parse(gameObj))
                    }

                    onShopsReceived.onReceived(status, message, games, paginate)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getSearchedShops(obj: JSONObject, onSearchedShopsReceived: onSearchedShopsReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.SHOP_SEARCH, obj, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSearchedShopsReceived.onReceived(status, message, ArrayList())
                    return
                }

                try {
                    val games = ArrayList<Game>()
                    if (status == 1) {
                        val data = response!!.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            games.add(Game.Parser.parse(data.getJSONObject(i)))
                        }
                    }

                    onSearchedShopsReceived.onReceived(status, message, games)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getRelatedShops(game_id: Int, onRelatedShopsReceived: onRelatedShopsReceived) {
        val url = Urls.GAME_RELATED_GAME_FOR_SHOP + "/" + Integer.toString(game_id)
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRelatedShopsReceived.onReceived(status, message, ArrayList<Game>())
                    return
                }

                try {
                    val games = ArrayList<Game>()
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        games.add(Game.Parser.parse(data.getJSONObject(i)))
                    }

                    onRelatedShopsReceived.onReceived(status, message, games)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    fun getSpecialShop(id: Int, onSpecialshopReceived: onSpecialShopReceived) {
        val url = Urls.SHOP_INDEX + "/" + Integer.toString(id)
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSpecialshopReceived.onReceived(status, message, Game())
                    return
                }

                var game = Game()
                try {
                    val gameObj = response!!.getJSONObject("data")
                    game = Game.Parser.parse(gameObj)

                    onSpecialshopReceived.onReceived(status, message, game)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onShopsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate)
    }

    interface onSearchedShopsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>)
    }

    interface onRelatedShopsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>)
    }

    interface onSpecialShopReceived {
        fun onReceived(status: Int, message: String, game: Game)
    }


}
