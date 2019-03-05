package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class Game {
    var id: Int = 0
    var game_info_id: Int = 0
    var price: Int = 0
    private var is_pending: Int = 0
    var name: String? = null
    var console_type_id: Int = 0
    var age_class: Int = 0
    var production_date: String? = null
    var region: String? = null
    var count: Int = 0
    private var can_play_online: Int = 0
    var company_name: String? = null
    var description: String? = null
    var photos: ArrayList<Photo>? = null
    var videos: ArrayList<Video>? = null
    var genres: ArrayList<String>? = null
    var console_name: String? = null
    var app_main_photos_url: ArrayList<String>? = null
    var app_cover_photo_url: String? = null

    object Parser {

        fun parse(jsonObject: JSONObject): Game {
            val game = Game()
            try {
                var game_info_obj = JSONObject()
                game_info_obj = jsonObject.getJSONObject("game_info")

                game.id = jsonObject.getInt("id")
                game.game_info_id = jsonObject.getInt("game_info_id")
                game.price = jsonObject.getInt("price")
                game.setIs_pending(jsonObject.getInt("is_pending"))
                game.region = jsonObject.getString("region")
                game.count = jsonObject.getInt("count")

                //game_info
                game.name = game_info_obj.getString("name")
                game.console_type_id = game_info_obj.getInt("console_type_id")
                game.age_class = game_info_obj.getInt("age_class")
                game.production_date = game_info_obj.getString("production_date")
                //        game.setRegion(game_info_obj.getString("region"));
                game.setCan_play_online(game_info_obj.getInt("can_play_online"))
                game.company_name = game_info_obj.getString("company_name")
                game.description = game_info_obj.getString("description")
                val photoArray = game_info_obj.getJSONArray("photos")
                val photos = ArrayList<Photo>()

                val main_photo_urls = ArrayList<String>()
                for (i in 0 until photoArray.length()) {
                    val photo: Photo
                    photo = Photo.Parser.parse(photoArray.getJSONObject(i))
                    photos.add(photo)

                    if (photo.type!!.contains("app_main")) {
                        main_photo_urls.add(photo.url)
                    } else if (photo.type!!.contains("app_cover")) {
                        game.app_cover_photo_url = photo.url
                    }

                }
                game.app_main_photos_url = main_photo_urls
                game.photos = photos

                val videoArray = game_info_obj.getJSONArray("videos")
                val videos = ArrayList<Video>()
                for (i in 0 until videoArray.length()) {
                    val video: Video
                    video = Video.Parser.parse(videoArray.getJSONObject(i))
                    videos.add(video)
                }
                game.videos = videos

                val genreArray = game_info_obj.getJSONArray("genres")
                val genres = ArrayList<String>()
                for (i in 0 until genreArray.length()) {
                    val genre: String
                    genre = genreArray.getJSONObject(i).getString("name")
                    genres.add(genre)
                }
                game.genres = genres

                game.console_name = game_info_obj.getJSONObject("console").getString("name")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return game
        }
    }


    fun isIs_pending(): Int {
        return is_pending
    }

    fun setIs_pending(is_pending: Int) {
        this.is_pending = is_pending
    }

    fun isCan_play_online(): Int {
        return can_play_online
    }

    fun setCan_play_online(can_play_online: Int) {
        this.can_play_online = can_play_online
    }


    fun getIs_pending(): Int {
        return is_pending
    }

    fun getCan_play_online(): Int {
        return can_play_online
    }


}
