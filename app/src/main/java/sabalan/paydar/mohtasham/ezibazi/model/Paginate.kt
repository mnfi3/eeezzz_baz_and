package sabalan.paydar.mohtasham.ezibazi.model

import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable

class Paginate : Serializable {

    var current_page: Int = 0
    var first_page_url: String? = null
    var from: Int = 0
    var last_page: Int = 0
    var last_page_url: String? = null
    var next_page_url: String? = null
    var prev_page_url: String? = null
    var path: String? = null
    var per_page: Int = 0
    var to: Int = 0
    var total: Int = 0

    class Parser : Serializable {
        companion object {
            fun parse(response: JSONObject): Paginate {
                val paginate = Paginate()
                try {
                    paginate.current_page = response.getInt("current_page")
                    paginate.first_page_url = response.getString("first_page_url")
                    paginate.from = response.getInt("from")
                    paginate.last_page = response.getInt("last_page")
                    paginate.last_page_url = response.getString("last_page_url")
                    paginate.next_page_url = response.getString("next_page_url")
                    paginate.path = response.getString("path")
                    paginate.per_page = response.getInt("per_page")
                    paginate.prev_page_url = response.getString("prev_page_url")
                    paginate.to = response.getInt("to")
                    paginate.total = response.getInt("total")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                return paginate
            }
        }
    }

}
