package mohtasham.paydar.sabalan.ezbazi.controller.api_service.user_actvity;

import android.content.Context;

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
import mohtasham.paydar.sabalan.ezbazi.model.RentRequest;
import mohtasham.paydar.sabalan.ezbazi.model.ShopRequest;

public class UserRequestsService {
  private Context context;
  public UserRequestsService(Context context){
    this.context = context;
  }


  public void getRentRequests( final onRentRequestsReceived onRentRequestsReceived){
    String url = Urls.USER_RENT_REQUESTS;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<RentRequest> requests =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject requestObj = data.getJSONObject(i);
            requests.add(RentRequest.Parser.parse(requestObj));
          }

          onRentRequestsReceived.onReceived(status, message, requests);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRentRequestsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<RentRequest>());

      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        //user_id=3
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjU1YmJhYjdhNzJmYTBiMGM5YTE5ZWM2YTRkMTE3MTI0ZDgwODBlZjBmMmQ3MDgxMTBhYTE0MWVlZTMyNzRlODI1OTc5MDUwMmM1OTAwMjliIn0.eyJhdWQiOiIzIiwianRpIjoiNTViYmFiN2E3MmZhMGIwYzlhMTllYzZhNGQxMTcxMjRkODA4MGVmMGYyZDcwODExMGFhMTQxZWVlMzI3NGU4MjU5NzkwNTAyYzU5MDAyOWIiLCJpYXQiOjE1Mzc5NTI0MzIsIm5iZiI6MTUzNzk1MjQzMiwiZXhwIjoxNTY5NDg4NDMyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.g_ZI4s6K3wQTVjEQtFRlibiCVdMA9fB7CHxKtqw--aAhE_Lb_kH2cmOeuSei9CSNzVWOvLHRLy6obuOpsxdKs0qiqeutkIVsxybQoVyMLhhRwgNKGU7YIJiuYMmUV7noWRDHacVaEaM0FtnWmL42ub-mkVie-V37LaEtSl9miXTh3ck8mFt5gj1LxNdl_N5_pjrjHG3Dc9D9XzrJ4OPAaGEqIi4__iEG9S_Z2s4S1_03KvObu0ygcw2Yd7NZjzqWN7JkDKQxTt-R46nCTPPfwUnoWBffR2nsHzcqf6woidJKKO15hmgbcTETOC-bce-VVgW2tU6MVxOlUoDeQ1LaUZ6aOtPvEkmPcbZgT8eswg-1W_bQnZ6SlsJunwLCT8HJeCifqD1_O1GHlAHxt_ZFGryCm1r4PwDQEDtd5Tt_S1vGthJ01Ev2jqrVXX7Ta2tn_G6kBMS24ItUboNKkVUFdULLEr-am-v5DSdKxtxvhz_sBqcOxgLmrubjzBmIOoH1IcqNlTw129rghGG8O3OQxyzizdo9pcBjQODB3QYZ5in6tl5j---3BNolu2LAeLutmFv3GZJMO4d54dOZeMbD375KdiXH80SZAY93lq-13iuzaw-MHKAdwU1J3eMqjGmr20gTI9RRqvACxXrQHctdyYdvMG1AQfHI4UVKOEUbO9k";
//        params.put("Authorization", "Bearer " + user.getToken());
        params.put("Authorization", "Bearer " + G.getUserToken());
        return params;
      }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void getShopRequests( final onShopRequestsReceived onShopRequestsReceived){
    String url = Urls.USER_BUY_REQUESTS;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<ShopRequest> requests =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject requestObj = data.getJSONObject(i);
            requests.add(ShopRequest.Parser.parse(requestObj));
          }

          onShopRequestsReceived.onReceived(status, message, requests);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onShopRequestsReceived.onReceived(0, "خطا در اتصال به سرور", new ArrayList<ShopRequest>());

      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        //user_id=3
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjU1YmJhYjdhNzJmYTBiMGM5YTE5ZWM2YTRkMTE3MTI0ZDgwODBlZjBmMmQ3MDgxMTBhYTE0MWVlZTMyNzRlODI1OTc5MDUwMmM1OTAwMjliIn0.eyJhdWQiOiIzIiwianRpIjoiNTViYmFiN2E3MmZhMGIwYzlhMTllYzZhNGQxMTcxMjRkODA4MGVmMGYyZDcwODExMGFhMTQxZWVlMzI3NGU4MjU5NzkwNTAyYzU5MDAyOWIiLCJpYXQiOjE1Mzc5NTI0MzIsIm5iZiI6MTUzNzk1MjQzMiwiZXhwIjoxNTY5NDg4NDMyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.g_ZI4s6K3wQTVjEQtFRlibiCVdMA9fB7CHxKtqw--aAhE_Lb_kH2cmOeuSei9CSNzVWOvLHRLy6obuOpsxdKs0qiqeutkIVsxybQoVyMLhhRwgNKGU7YIJiuYMmUV7noWRDHacVaEaM0FtnWmL42ub-mkVie-V37LaEtSl9miXTh3ck8mFt5gj1LxNdl_N5_pjrjHG3Dc9D9XzrJ4OPAaGEqIi4__iEG9S_Z2s4S1_03KvObu0ygcw2Yd7NZjzqWN7JkDKQxTt-R46nCTPPfwUnoWBffR2nsHzcqf6woidJKKO15hmgbcTETOC-bce-VVgW2tU6MVxOlUoDeQ1LaUZ6aOtPvEkmPcbZgT8eswg-1W_bQnZ6SlsJunwLCT8HJeCifqD1_O1GHlAHxt_ZFGryCm1r4PwDQEDtd5Tt_S1vGthJ01Ev2jqrVXX7Ta2tn_G6kBMS24ItUboNKkVUFdULLEr-am-v5DSdKxtxvhz_sBqcOxgLmrubjzBmIOoH1IcqNlTw129rghGG8O3OQxyzizdo9pcBjQODB3QYZ5in6tl5j---3BNolu2LAeLutmFv3GZJMO4d54dOZeMbD375KdiXH80SZAY93lq-13iuzaw-MHKAdwU1J3eMqjGmr20gTI9RRqvACxXrQHctdyYdvMG1AQfHI4UVKOEUbO9k";
//        params.put("Authorization", "Bearer " + user.getToken());
        params.put("Authorization", "Bearer " + G.getUserToken());
        return params;
      }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }










  public interface onRentRequestsReceived{
    void onReceived(int status, String message, List<RentRequest> requests);
  }

  public interface onShopRequestsReceived{
    void onReceived(int status, String message, List<ShopRequest> requests);
  }



}
