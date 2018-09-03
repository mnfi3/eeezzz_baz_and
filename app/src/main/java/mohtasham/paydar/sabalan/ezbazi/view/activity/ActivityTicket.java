package mohtasham.paydar.sabalan.ezbazi.view.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.TicketAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.ticket.TicketService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Ticket;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ActivityTicket extends AppCompatActivity {

  RecyclerView rcv_tickets;
  EditText edt_message;
  ImageButton imb_send_message;
  TicketService apiService;
  TicketAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket);

    setupViews();
    LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityTicket.this, LinearLayoutManager.VERTICAL, false);
    rcv_tickets.setLayoutManager(layoutManager);
    getTickets();
    setSeenTickets();

    imb_send_message.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(checkEntry()){
          sendTicket();
        }
      }
    });

  }

  private void setupViews(){
    rcv_tickets = (RecyclerView) findViewById(R.id.rcv_tickets);
    edt_message = (EditText) findViewById(R.id.edt_message);
    imb_send_message = (ImageButton) findViewById(R.id.imb_send_message);
  }

  private void getTickets(){
    apiService = new TicketService(ActivityTicket.this);
    apiService.getTickets(new TicketService.onTicketsReceived() {
      @Override
      public void onReceived(int status, String message, Paginate paginate, final List<Ticket> tickets) {
        if (status == 0){
          MyViews.makeText(ActivityTicket.this, message, Toast.LENGTH_SHORT);
        }else {
          adapter = new TicketAdapter(G.context, tickets);
          rcv_tickets.setAdapter(adapter);
          if (tickets.size() > 0) {
//            rcv_tickets.smoothScrollToPosition(tickets.size() - 1);
            rcv_tickets.smoothScrollToPosition(adapter.getItemCount());
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
      apiService = new TicketService(ActivityTicket.this);
      apiService.sendTicket(object, new TicketService.onSendTicket() {
        @Override
        public void onReceived(int status, String message, Ticket ticket) {
          if (status == 1){
            edt_message.setText("");
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket);
            adapter.notifyData(tickets);
            rcv_tickets.smoothScrollToPosition(adapter.getItemCount());
          }else {
            MyViews.makeText(ActivityTicket.this, message, Toast.LENGTH_SHORT);
          }
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
