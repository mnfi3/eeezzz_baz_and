package mohtasham.paydar.sabalan.ezbazi.controller.api_service.account;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.Urls;
import mohtasham.paydar.sabalan.ezbazi.controller.system.application.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.auth.Auth;
import mohtasham.paydar.sabalan.ezbazi.model.User;

public class AccountService {
  private Context context;
  public AccountService(Context context){
    this.context = context;
  }


  public void login(JSONObject jsonObject, final onLoginComplete onLoginComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.LOGIN, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String token = "";
        User user = new User();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          if(status == 1) {
            token = response.getJSONObject("data").getString("token");
            JSONObject userObj = response.getJSONObject("data").getJSONObject("user");
            user = User.Parser.parse(userObj);
            user.setToken(token);
          }
          onLoginComplete.onComplete(status, message, token, user);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onLoginComplete.onComplete(0, "خطا در ارتباط با سرور", "", new User());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void register(JSONObject jsonObject, final onRegisterComplete onRegisterComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.REGISTER, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String token = "";
        User user = new User();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          token = response.getJSONObject("data").getString("token");
          JSONObject userObj = response.getJSONObject("data").getJSONObject("user");

          user = User.Parser.parse(userObj);

          onRegisterComplete.onComplete(status, message, token, user);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        onRegisterComplete.onComplete(0, "خطا در ارتباط با سرور", "", new User());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void logout(final onLogoutComplete onLogoutComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.LOGOUT, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          onLogoutComplete.onComplete(status, message);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onLogoutComplete.onComplete(0, "خطا در ارتباط با سرور");
      }
    })
    {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + Auth.getUserToken());
        return params;
      }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }







  public interface onLoginComplete{
    void onComplete(int status, String message, String token, User user);
  }

  public interface onLogoutComplete{
    void onComplete(int status, String message);
  }


  public interface onRegisterComplete{
    void onComplete(int status, String message, String token, User user);
  }


}
