package mohtasham.paydar.sabalan.ezbazi.controller.api_service.ticket;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.Urls;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Ticket;

public class TicketService {
  private Context context;
  public TicketService(Context context){
    this.context = context;
  }


  public void getTickets(final onTicketsReceived onTicketsReceived){
    Volley.newRequestQueue(context).cancelAll("get_tickets");
    Log.i("getTickets", "getTickets: " + "request");
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.TICKET_INDEX, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        Paginate paginate;
        List<Ticket> tickets = new ArrayList<Ticket>();
        try {
          status = response.getInt("status");
          message = response.getString("message");
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
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onTicketsReceived.onReceived(0, "خطا در ارتباط با سرور", new Paginate(), new ArrayList<Ticket>());
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + G.getUserToken());
        return params;
      }
    };



    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request).setTag("get_tickets");
  }



  public void seenTickets(final onSeenTicket onSeenTicket){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.TICKET_SEEN, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          onSeenTicket.onReceived(status, message);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSeenTicket.onReceived(0, "خطا در ارتباط با سرور");
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + G.getUserToken());
        return params;
      }
    };



    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }




  public void sendTicket(JSONObject jsonObject, final onSendTicket onSendTicket){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.TICKET_INDEX, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        Ticket ticket;
        try {
          status = response.getInt("status");
          message = response.getString("message");
          ticket = Ticket.Parser.parse(response.getJSONObject("data"));
          onSendTicket.onReceived(status, message, ticket);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSendTicket.onReceived(0, "خطا در ارتباط با سرور", new Ticket());
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + G.getUserToken());
        return params;
      }
    };



    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
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

}
