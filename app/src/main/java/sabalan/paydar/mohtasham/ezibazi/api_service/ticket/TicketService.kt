package sabalan.paydar.mohtasham.ezibazi.api_service.ticket

import android.content.Context
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.Ticket

class TicketService(private val context: Context) {


    fun getTickets(onTicketsReceived: onTicketsReceived) {
        Volley.newRequestQueue(context).cancelAll("get_tickets")
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.TICKET_INDEX, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onTicketsReceived.onReceived(status, message, Paginate(), ArrayList<Ticket>())
                    return
                }

                val paginate: Paginate
                val tickets = ArrayList<Ticket>()
                try {
                    if (status == 0) {
                        onTicketsReceived.onReceived(status, message, Paginate(), ArrayList<Ticket>())
                    } else {
                        val jsonData = response!!.getJSONObject("data")
                        paginate = Paginate.Parser.parse(jsonData)
                        val data = jsonData.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val ticket = Ticket.Parser.parse(data.get(i) as JSONObject)
                            tickets.add(ticket)
                        }

                        onTicketsReceived.onReceived(status, message, paginate, tickets)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }, "get_tickets")


    }


    fun getNewTicketsCount(onNewTicketsCount: onNewTicketsCount) {
        Volley.newRequestQueue(context).cancelAll("get_new_tickets_count")
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.NEW_TICKETS_COUNT, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onNewTicketsCount.onReceived(status, message, 0)
                    return
                }

                var count = 0
                try {
                    count = response!!.getInt("data")
                    onNewTicketsCount.onReceived(status, message, count)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }, "get_new_tickets_count")


    }


    fun seenTickets(onSeenTicket: onSeenTicket) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.TICKET_SEEN, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSeenTicket.onReceived(status, message)
                    return
                }

                onSeenTicket.onReceived(status, message)
            }
        })


    }


    fun sendTicket(jsonObject: JSONObject, onSendTicket: onSendTicket) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.POST, Urls.TICKET_INDEX, jsonObject, true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onSendTicket.onReceived(status, message, Ticket())
                    return
                }

                val ticket: Ticket
                try {
                    ticket = Ticket.Parser.parse(response!!.getJSONObject("data"))
                    onSendTicket.onReceived(status, message, ticket)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })


    }


    interface onTicketsReceived {
        fun onReceived(status: Int, message: String, paginate: Paginate, tickets: List<Ticket>)
    }

    interface onSeenTicket {
        fun onReceived(status: Int, message: String)
    }


    interface onSendTicket {
        fun onReceived(status: Int, message: String, ticket: Ticket)
    }

    interface onNewTicketsCount {
        fun onReceived(status: Int, message: String, count: Int)
    }

}
