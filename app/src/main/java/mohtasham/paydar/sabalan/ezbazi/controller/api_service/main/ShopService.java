package mohtasham.paydar.sabalan.ezbazi.controller.api_service.main;

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

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.Urls;
import mohtasham.paydar.sabalan.ezbazi.controller.system.pref_manager.CityPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;

public class ShopService {
  private Context context;
  public ShopService(Context context){
    this.context = context;
  }


  public void getMainShops(final int page_num, final onShopsReceived onShopsReceived){
    String url = Urls.SHOP_INDEX0 + "/"+ Integer.toString(new CityPrefManager(context).getCityId()) + "?page=" + page_num;
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


          onShopsReceived.onReceived(status, message, games, paginate);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onShopsReceived.onReceived(0, "", new ArrayList<Game>(), new Paginate());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSearchedShops(JSONObject obj, final onSearchedShopsReceived onSearchedShopsReceived){
    String url = Urls.SHOP_SEARCH;
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

          onSearchedShopsReceived.onReceived(status, message, games);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSearchedShopsReceived.onReceived(0, "خطا در ارتباط با سرور", new ArrayList<Game>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getRelatedShops(final int game_id, final onRelatedShopsReceived onRelatedShopsReceived){
    String url = Urls.GAME_RELATED_GAME_FOR_SHOP + "/"+Integer.toString(game_id) ;
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


          onRelatedShopsReceived.onReceived(status, message, games);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRelatedShopsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<Game>());

      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSpecialShop(int id, final onSpecialShopReceived onSpecialshopReceived){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.SHOP_INDEX + "/" + id, null, new Response.Listener<JSONObject>() {
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

          onSpecialshopReceived.onReceived(status, message, game);

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSpecialshopReceived.onReceived(0, "خطا در ارتباط با سرور", new Game());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public interface onShopsReceived{
    void onReceived(int status, String message, List<Game> games, Paginate paginate);
  }

  public interface onSearchedShopsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onRelatedShopsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onSpecialShopReceived{
    void onReceived(int status, String message, Game game);
  }


}
