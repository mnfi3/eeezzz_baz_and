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
import sabalan.paydar.mohtasham.ezibazi.model.RentType

class RentService(private val context: Context) {


    fun getRents(page_num: Int, onRentsReceived: onRentsReceived) {
        val url = Urls.RENT_INDEX0 + "/" + Integer.toString(CityPrefManager(context).cityId) + "?page=" + page_num
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRentsReceived.onReceived(status, message, ArrayList<Game>(), Paginate())
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

                    onRentsReceived.onReceived(status, message, games, paginate)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getSearchedRents(obj: JSONObject, onSearchedRentsReceived: onSearchedRentsReceived) {
        val url = Urls.RENT_SEARCH
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.RENT_SEARCH, obj, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSearchedRentsReceived.onReceived(status, message, ArrayList())
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

                    onSearchedRentsReceived.onReceived(status, message, games)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    fun getRelatedRents(game_id: Int, onRelatedRentsReceived: onRelatedRentsReceived) {
        val url = Urls.GAME_RELATED_GAME_FOR_RENT + "/" + Integer.toString(game_id)
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onRelatedRentsReceived.onReceived(status, message, ArrayList<Game>())
                    return
                }

                try {
                    val games = ArrayList<Game>()
                    val data = response!!.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        games.add(Game.Parser.parse(data.getJSONObject(i)))
                    }

                    onRelatedRentsReceived.onReceived(status, message, games)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getSpecialRent(id: Int, onSpecialRentReceived: onSpecialRentReceived) {
        val url = Urls.RENT_INDEX + "/" + Integer.toString(id)
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, url, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                var status = status
                var message = message
                if (error) {
                    onSpecialRentReceived.onReceived(status, message, Game())
                    return
                }

                val game: Game
                try {
                    status = response!!.getInt("status")
                    message = response.getString("message")
                    val gameObj = response.getJSONObject("data")
                    game = Game.Parser.parse(gameObj)

                    onSpecialRentReceived.onReceived(status, message, game)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    fun getRentTypes(onRentTypesReceived: onRentTypesReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.RENT_TYPE_INDEX, null!!, false, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                var status = status
                var message = message
                if (error) {
                    onRentTypesReceived.onReceived(status, message, ArrayList<RentType>())
                    return
                }

                val rentTypes = ArrayList<RentType>()
                try {
                    status = response!!.getInt("status")
                    message = response.getString("message")
                    val data = response.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val rentTypeObj = data.getJSONObject(i)
                        rentTypes.add(RentType.Parser.parse(rentTypeObj))
                    }

                    onRentTypesReceived.onReceived(status, message, rentTypes)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

    }


    interface onRentsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate)
    }

    interface onSearchedRentsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>)
    }

    interface onRelatedRentsReceived {
        fun onReceived(status: Int, message: String, games: List<Game>)
    }

    interface onSpecialRentReceived {
        fun onReceived(status: Int, message: String, game: Game)
    }

    interface onRentTypesReceived {
        fun onReceived(status: Int, message: String, rentTypes: List<RentType>)
    }

}
