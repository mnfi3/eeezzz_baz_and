package sabalan.paydar.mohtasham.ezibazi.view.activity;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.api_service.ticket.TicketService;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.Ticket;
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.TicketAdapter;
import sabalan.paydar.mohtasham.ezibazi.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ActivityTicket extends AppCompatActivity {

  TextView txt_page_name;
  ImageView img_back;
  RecyclerView rcv_tickets;
  EditText edt_message;
  ImageButton imb_send_message;
  TicketService apiService;
  TicketAdapter adapter;
  AVLoadingIndicatorView avl_send_message;
  int first_ticket_id = -1;
  boolean is_create_adapter = false;
  boolean is_pause = false;
  Handler handler;
  Runnable runnable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket);

    setupViews();
    setTypeFace();


    txt_page_name.setText("ارتباط با پشتیبانی");
    LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityTicket.this, LinearLayoutManager.VERTICAL, false);
//    layoutManager.setReverseLayout(true);
    rcv_tickets.setLayoutManager(layoutManager);
    avl_send_message.setVisibility(View.VISIBLE);
    setSeenTickets();

    getTickets();

    //get tickets with async
//    receiveTickets();

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    imb_send_message.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(checkEntry()){
          avl_send_message.setVisibility(View.VISIBLE);
          sendTicket();
        }
      }
    });






  }

  private void setupViews(){
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
    rcv_tickets = findViewById(R.id.rcv_tickets);
    edt_message =  findViewById(R.id.edt_message);
    imb_send_message =  findViewById(R.id.imb_send_message);
    avl_send_message = findViewById(R.id.avl_send_message);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityTicket.this));
  }



  private void getTickets(){
    apiService = new TicketService(ActivityTicket.this);
    apiService.getTickets(new TicketService.onTicketsReceived() {
      @Override
      public void onReceived(int status, String message, Paginate paginate, final List<Ticket> tickets) {
        avl_send_message.setVisibility(View.INVISIBLE);

        if (status == 0){
//          MyViews.makeText(ActivityTicket.this, message, Toast.LENGTH_SHORT);
        }else {


          if (tickets.size()>0) {

            if(first_ticket_id != tickets.get(0).getId()) {
              first_ticket_id = tickets.get(0).getId();
              Collections.reverse(tickets);
              adapter = new TicketAdapter(ActivityTicket.this, new ArrayList<Ticket>());
              is_create_adapter = true;
              rcv_tickets.setAdapter(adapter);
              adapter.notifyData(tickets);
              rcv_tickets.scrollToPosition(adapter.getItemCount() - 1);
              setSeenTickets();
            }
          }

        }
      }
    });

  }


  private void setSeenTickets(){
    apiService = new TicketService(ActivityTicket.this);
    apiService.seenTickets(new TicketService.onSeenTicket() {
      @Override
      public void onReceived(int status, String message) {
       //no thing :)
      }
    });
  }

  private boolean checkEntry(){
    if(edt_message.getText().toString().length() < 2){
      MyViews.makeText(ActivityTicket.this, "متن شما بسیار کوتاه است", Toast.LENGTH_SHORT);
      return false;
    }
    else if(edt_message.getText().toString().length() > 998){
      MyViews.makeText(ActivityTicket.this, "متن شما بسیار طولانی است", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }

  private void sendTicket(){
    JSONObject object = new JSONObject();
    try {
      object.put("text", edt_message.getText().toString());
      object.put("is_user_sent",1);
      edt_message.setText("");
      apiService = new TicketService(ActivityTicket.this);
      apiService.sendTicket(object, new TicketService.onSendTicket() {
        @Override
        public void onReceived(int status, String message, Ticket ticket) {
          avl_send_message.setVisibility(View.INVISIBLE);
          if (status == 1){
//            getTickets();

            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket);

            //for first ticket send
            if(!is_create_adapter){
              adapter = new TicketAdapter(ActivityTicket.this, new ArrayList<Ticket>());
              getTickets();
              return;
            }

            first_ticket_id = ticket.getId();
            adapter.notifyData(tickets);

            rcv_tickets.smoothScrollToPosition(adapter.getItemCount() - 1);
          }else {
            MyViews.makeText(ActivityTicket.this, message, Toast.LENGTH_SHORT);
          }
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }
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


  @Override
  protected void onPause() {
    super.onPause();
//    is_pause = true;
//    handler.removeCallbacks(runnable);
  }

  @Override
  protected void onResume() {
    super.onResume();
//    getTickets();
//    receiveTickets();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
//    getTickets();
//    receiveTickets();
  }

  protected void onStart() {
    super.onStart();


//    connectivityListener = new ConnectivityListener(lyt_action);
    registerReceiver(G.connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();
//    handler.removeCallbacks(runnable);
    unregisterReceiver(G.connectivityListener);
  }
}
