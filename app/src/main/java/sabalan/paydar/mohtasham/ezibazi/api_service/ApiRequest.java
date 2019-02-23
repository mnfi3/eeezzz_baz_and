package sabalan.paydar.mohtasham.ezibazi.api_service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sabalan.paydar.mohtasham.ezibazi.system.auth.Auth;
import sabalan.paydar.mohtasham.ezibazi.system.config.AppConfig;

public class ApiRequest {

  private static String TAG = "ApiRequest";


  public static final int GET = 0;
  public static final int POST = 1;
  public static final int PUT = 2;
  public static final int DELETE = 3;
  public static final int HEAD = 4;
  public static final int OPTION = 5;
  public static final int TRACE = 6;
  public static final int PATCH = 7;

  private Context context;

  private static long last_request_time = 0;


  public ApiRequest(Context context){
    this.context = context;
  }


  //json object request
  public void request(int method, String url, JSONObject object, final boolean auth, final onDataReceived listener){

    final JsonObjectRequest request = new JsonObjectRequest(method, url, object, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        listener.onReceived(response, status, message, false);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        listener.onReceived(null, 0, "خطا در ارتباط با سرور", true);
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Content-Type", "application/json");
        if(auth) params.put("Authorization", "Bearer " + Auth.getUserToken());
        return params;
      }
    };


    request.setRetryPolicy(new DefaultRetryPolicy(AppConfig.DEFAULT_VOLLEY_TIME_OUT_MILLIS, AppConfig.DEFAULT_VOLLEY_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }






  public void request(int method, String url, JSONObject object, final boolean auth, final onDataReceived listener, String tag){

    final JsonObjectRequest request = new JsonObjectRequest(method, url, object, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        listener.onReceived(response, status, message, false);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        listener.onReceived(null, 0, "خطا در ارتباط با سرور", true);
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Content-Type", "application/json");
        if(auth) params.put("Authorization", "Bearer " + Auth.getUserToken());
        return params;
      }
    };


    request.setRetryPolicy(new DefaultRetryPolicy(AppConfig.DEFAULT_VOLLEY_TIME_OUT_MILLIS, AppConfig.DEFAULT_VOLLEY_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request).setTag(tag);
  }










  public interface onDataReceived{
    void onReceived(JSONObject response, int status, String message, boolean error);
  }
}
