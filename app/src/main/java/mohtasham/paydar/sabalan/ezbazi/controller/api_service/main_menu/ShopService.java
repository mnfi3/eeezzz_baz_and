package mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu;

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
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;

public class ShopService {
  private Context context;
  public ShopService(Context context){
    this.context = context;
  }


  public void getMainShops(final int page_num, final onShopsReceived onShopsReceived){
    String url = Urls.SHOP_INDEX + "?page=" + page_num;
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

  public interface onShopsReceived{
    void onReceived(int status, String message, List<Game> games, Paginate paginate);
  }


}
