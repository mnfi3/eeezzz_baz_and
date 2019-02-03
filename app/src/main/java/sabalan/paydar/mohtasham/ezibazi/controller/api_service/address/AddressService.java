package sabalan.paydar.mohtasham.ezibazi.controller.api_service.address;

import android.content.Context;
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

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.AccountService;
import sabalan.paydar.mohtasham.ezibazi.model.Address;
import sabalan.paydar.mohtasham.ezibazi.model.City;
import sabalan.paydar.mohtasham.ezibazi.model.State;
import sabalan.paydar.mohtasham.ezibazi.model.User;

public class AddressService {
  private Context context;
  public AddressService(Context context){
    this.context = context;
  }




  public void getStates(final onStatesReceived onStatesReceived){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.ADDRESS_STATES, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String token = "";
        ArrayList<State> states = new ArrayList<State>();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONArray data = response.getJSONArray("data");

          for (int i=0 ; i< data.length() ; i++){
            State state;
            state = State.Parser.parse(data.getJSONObject(i));
            states.add(state);
          }
          onStatesReceived.onComplete(status, message, states);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        onStatesReceived.onComplete(0, "مشکل در ارتباط با سرور", new ArrayList<State>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }




  public void getStateCities(int state_id, final onStateCitiesReceived onStateCitiesReceived){
    String url = Urls.ADDRESS_STATE_CITIES;
    String id = Integer.toString(state_id);
    url = url.replace("{id}", id);
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        ArrayList<City> cities = new ArrayList<City>();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONArray data = response.getJSONArray("data");

          for (int i=0 ; i< data.length() ; i++){
            City city;
            city = City.Parser.parse(data.getJSONObject(i));
            cities.add(city);
          }
          onStateCitiesReceived.onComplete(status, message, cities);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        onStateCitiesReceived.onComplete(0, "مشکل در ارتباط با سرور", new ArrayList<City>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void saveAddress(JSONObject jsonObject, final onAddressSaved onAddressSaved){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.ADDRESS_INDEX, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        Address address = new Address();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          address = Address.Parser.parse(response.getJSONObject("data"));
          onAddressSaved.onComplete(status, message, address);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onAddressSaved.onComplete(0, "خطا در ارتباط با سرور", new Address());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }










  public interface onStatesReceived{
    void onComplete(int status, String message, ArrayList<State> states);
  }


  public interface onStateCitiesReceived{
    void onComplete(int status, String message, ArrayList<City> cities);
  }


  public interface onAddressSaved{
    void onComplete(int status, String message, Address address);
  }


}
