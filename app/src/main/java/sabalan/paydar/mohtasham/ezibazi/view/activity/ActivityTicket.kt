package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.wang.avi.AVLoadingIndicatorView
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.ticket.TicketService
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.Ticket
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.TicketAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import java.util.*

class ActivityTicket : AppCompatActivity() {

    internal lateinit var txt_page_name: TextView
    internal lateinit var img_back: ImageView
    internal lateinit var rcv_tickets: RecyclerView
    internal lateinit var edt_message: EditText
    internal lateinit var imb_send_message: ImageButton
    internal lateinit var apiService: TicketService
    internal lateinit var adapter: TicketAdapter
    internal lateinit var avl_send_message: AVLoadingIndicatorView
    internal var first_ticket_id = -1
    internal var is_create_adapter = false
    internal var is_pause = false
    internal var handler: Handler? = null
    internal var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        setupViews()
        setTypeFace()


        txt_page_name.text = "ارتباط با پشتیبانی"
        val layoutManager = LinearLayoutManager(this@ActivityTicket, LinearLayoutManager.VERTICAL, false)
        //    layoutManager.setReverseLayout(true);
        rcv_tickets.layoutManager = layoutManager
        avl_send_message.visibility = View.VISIBLE
        MyViews.freezeEnable(this@ActivityTicket)
        setSeenTickets()

        getTickets()

        //get tickets with async
        //    receiveTickets();

        img_back.setOnClickListener { finish() }

        imb_send_message.setOnClickListener {
            if (checkEntry()) {
                avl_send_message.visibility = View.VISIBLE
                MyViews.freezeEnable(this@ActivityTicket)
                sendTicket()
            }
        }


    }

    private fun setupViews() {
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)
        rcv_tickets = findViewById(R.id.rcv_tickets)
        edt_message = findViewById(R.id.edt_message)
        imb_send_message = findViewById(R.id.imb_send_message)
        avl_send_message = findViewById(R.id.avl_send_message)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityTicket)
    }


    private fun getTickets() {
        apiService = TicketService(this@ActivityTicket)
        val onTicketsReceived = object : TicketService.onTicketsReceived{
            override fun onReceived(status: Int, message: String, paginate: Paginate, tickets: List<Ticket>) {
                avl_send_message.visibility = View.INVISIBLE
                MyViews.freezeDisable(this@ActivityTicket)

                if (status == 0) {
                    //          MyViews.makeText(ActivityTicket.this, message, Toast.LENGTH_SHORT);
                } else {


                    if (tickets.size > 0) {

                        if (first_ticket_id != tickets[0].id) {
                            first_ticket_id = tickets[0].id
                            Collections.reverse(tickets)
                            adapter = TicketAdapter(this@ActivityTicket, ArrayList())
                            is_create_adapter = true
                            rcv_tickets.adapter = adapter
                            adapter.notifyData(tickets)
                            rcv_tickets.scrollToPosition(adapter.itemCount - 1)
                            setSeenTickets()
                        }
                    }

                }
            }
        }

        apiService.getTickets(onTicketsReceived)

    }


    private fun setSeenTickets() {
        apiService = TicketService(this@ActivityTicket)
        val onSeenTicket = object : TicketService.onSeenTicket{
            override fun onReceived(status: Int, message: String) {
                //remove notification count from app icon shortcut
                ShortcutBadger.removeCount(this@ActivityTicket)
            }
        }

        apiService.seenTickets(onSeenTicket)
    }

    private fun checkEntry(): Boolean {
        if (edt_message.text.toString().length < 2) {
            MyViews.makeText(this@ActivityTicket, "متن شما بسیار کوتاه است", Toast.LENGTH_SHORT)
            return false
        } else if (edt_message.text.toString().length > 998) {
            MyViews.makeText(this@ActivityTicket, "متن شما بسیار طولانی است", Toast.LENGTH_SHORT)
            return false
        }
        return true
    }

    private fun sendTicket() {
        val `object` = JSONObject()
        `object`.put("text", edt_message.text.toString())
        edt_message.setText("")
        apiService = TicketService(this@ActivityTicket)
        val onSendTicket = object : TicketService.onSendTicket {
            override fun onReceived(status: Int, message: String, ticket: Ticket) {
                avl_send_message.visibility = View.INVISIBLE
                MyViews.freezeDisable(this@ActivityTicket)
                if (status == 1) {
                    val tickets = ArrayList<Ticket>()
                    tickets.add(ticket)
                    //for first ticket send
                    if (!is_create_adapter) {
                        adapter = TicketAdapter(this@ActivityTicket, ArrayList())
                        getTickets()
                        return
                    }

                    first_ticket_id = ticket.id
                    adapter.notifyData(tickets)

                    rcv_tickets.smoothScrollToPosition(adapter.itemCount - 1)
                } else {
                    MyViews.makeText(this@ActivityTicket, message, Toast.LENGTH_SHORT)
                }
            }
        }

        apiService.sendTicket(`object`, onSendTicket)

    }


    //  private void receiveTickets(){
    //    handler = new Handler();
    //    runnable = new Runnable() {
    //      public void run() {
    //        getTickets();
    //        if (G.isLoggedIn) {
    //          handler.postDelayed(this, AppConfig.NEW_TICKETS_CHECK_TIME_MS);
    //        }else {
    //          handler.removeCallbacks(runnable);
    //          return;
    //        }
    //      }
    //    };
    //    handler.post(runnable);
    //
    //  }


    override fun onPause() {
        super.onPause()
        //    is_pause = true;
        //    handler.removeCallbacks(runnable);
    }

    override fun onResume() {
        super.onResume()
        //    getTickets();
        //    receiveTickets();
    }

    override fun onRestart() {
        super.onRestart()
        //    getTickets();
        //    receiveTickets();
    }

    override fun onStart() {
        super.onStart()


        //    connectivityListener = new ConnectivityListener(lyt_action);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()
        //    handler.removeCallbacks(runnable);
        unregisterReceiver(G.connectivityListener)
    }
}
