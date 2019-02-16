package sabalan.paydar.mohtasham.ezibazi.controller.api_service.ticket;

import android.content.Context;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.Ticket;

public class TicketService {
  private Context context;
  public TicketService(Context context){
    this.context = context;
  }


  public void getTickets(final onTicketsReceived onTicketsReceived){
    Volley.newRequestQueue(context).cancelAll("get_tickets");
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.TICKET_INDEX, null, true, new ApiRequest.onDataReceived() {
        @Override
        public void onReceived(JSONObject response, int status, String message, boolean error) {
          if (error){
            onTicketsReceived.onReceived(status, message, new Paginate(), new ArrayList<Ticket>());
            return;
          }

          Paginate paginate;
          List<Ticket> tickets = new ArrayList<Ticket>();
          try {
            if (status == 0){
              onTicketsReceived.onReceived(status, message, new Paginate(), new ArrayList<Ticket>());
            }else {
              JSONObject jsonData = response.getJSONObject("data");
              paginate = Paginate.Parser.parse(jsonData);
              JSONArray data = jsonData.getJSONArray("data");
              for (int i=0 ; i<data.length() ; i++){
                Ticket ticket = Ticket.Parser.parse((JSONObject) data.get(i));
                tickets.add(ticket);
              }

              onTicketsReceived.onReceived(status, message, paginate, tickets);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
    }, "get_tickets");


  }



  public void getNewTicketsCount(final onNewTicketsCount onNewTicketsCount){
    Volley.newRequestQueue(context).cancelAll("get_new_tickets_count");
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.NEW_TICKETS_COUNT, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onNewTicketsCount.onReceived(status, message, 0);
          return;
        }

        int count = 0;
        try {
          count = response.getInt("data");
          onNewTicketsCount.onReceived(status, message, count);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, "get_new_tickets_count");


  }



  public void seenTickets(final onSeenTicket onSeenTicket){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.TICKET_SEEN, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSeenTicket.onReceived(status, message);
          return;
        }

        onSeenTicket.onReceived(status, message);
      }
    });



  }




  public void sendTicket(JSONObject jsonObject, final onSendTicket onSendTicket){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.TICKET_INDEX, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSendTicket.onReceived(status, message, new Ticket());
          return;
        }

        Ticket ticket;
        try {
          ticket = Ticket.Parser.parse(response.getJSONObject("data"));
          onSendTicket.onReceived(status, message, ticket);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }





  public interface onTicketsReceived{
    void onReceived(int status, String message, Paginate paginate, List<Ticket> tickets);
  }

  public interface onSeenTicket{
    void onReceived(int status, String message);
  }


  public interface onSendTicket{
    void onReceived(int status, String message, Ticket ticket);
  }

  public interface onNewTicketsCount{
    void onReceived(int status, String message, int count);
  }

}
