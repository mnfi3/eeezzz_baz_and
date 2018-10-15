package sabalan.paydar.mohtasham.ezibazi.controller.api_service.main;

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
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.CityPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;

public class RentService {
  private Context context;
  public RentService(Context context){
    this.context = context;
  }


  public void getRents(final int page_num, final onRentsReceived onRentsReceived){
    String url = Urls.RENT_INDEX0 + "/"+ Integer.toString(new CityPrefManager(context).getCityId()) + "?page=" + page_num;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<Game> games =  new ArrayList<>();
          JSONObject jsonData = response.getJSONObject("data");
          Paginate paginate = Paginate.Parser.parse(jsonData);
          JSONArray data = jsonData.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject gameObj = data.getJSONObject(i);
            games.add( Game.Parser.parse(gameObj));
          }

          onRentsReceived.onReceived(status, message, games, paginate);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRentsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<Game>(), new Paginate());

      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void getSearchedRents(JSONObject obj, final onSearchedRentsReceived onSearchedRentsReceived){
    String url = Urls.RENT_SEARCH;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<Game> games =  new ArrayList<>();
          if(status == 1) {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
              games.add(Game.Parser.parse(data.getJSONObject(i)));
            }
          }


          onSearchedRentsReceived.onReceived(status, message, games);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSearchedRentsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<Game>());

      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void getRelatedRents(final int game_id, final onRelatedRentsReceived onRelatedRentsReceived){
    String url = Urls.GAME_RELATED_GAME_FOR_RENT + "/"+Integer.toString(game_id) ;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<Game> games =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for(int i=0 ; i<data.length();i++){
            games.add(Game.Parser.parse(data.getJSONObject(i)));
          }


          onRelatedRentsReceived.onReceived(status, message, games);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRelatedRentsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<Game>());

      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSpecialRent(int id, final onSpecialRentReceived onSpecialRentReceived){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.RENT_INDEX + "/" + id, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        Game game = new Game();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONObject gameObj = response.getJSONObject("data");

          game = Game.Parser.parse(gameObj);

          onSpecialRentReceived.onReceived(status, message, game);

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSpecialRentReceived.onReceived(0, "خطا در ارتباط با سرور", new Game());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }




  public void getRentTypes(final onRentTypesReceived onRentTypesReceived){
    String url = Urls.RENT_TYPE_INDEX ;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        List<RentType> rentTypes = new ArrayList<>();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject rentTypeObj = data.getJSONObject(i);
            rentTypes.add( RentType.Parser.parse(rentTypeObj));
          }

          onRentTypesReceived.onReceived(status, message, rentTypes);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRentTypesReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<RentType>());

      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }




  public interface onRentsReceived{
    void onReceived(int status, String message, List<Game> games, Paginate paginate);
  }

  public interface onSearchedRentsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onRelatedRentsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onSpecialRentReceived{
    void onReceived(int status, String message, Game game);
  }

  public interface onRentTypesReceived{
    void onReceived(int status, String message, List<RentType> rentTypes);
  }

}
