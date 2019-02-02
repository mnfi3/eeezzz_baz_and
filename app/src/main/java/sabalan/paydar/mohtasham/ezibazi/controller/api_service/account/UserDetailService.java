package sabalan.paydar.mohtasham.ezibazi.controller.api_service.account;

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

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.auth.Auth;
import sabalan.paydar.mohtasham.ezibazi.model.Address;
import sabalan.paydar.mohtasham.ezibazi.model.City;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
import sabalan.paydar.mohtasham.ezibazi.model.State;
import sabalan.paydar.mohtasham.ezibazi.model.User;

public class UserDetailService {
  private Context context;
  public UserDetailService(Context context){
    this.context = context;
  }


//  public void getUserDetail(JSONObject jsonObject, final onUserDetailReceivedComplete onUserDetailReceivedComplete){
//    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.LOGIN, jsonObject, new Response.Listener<JSONObject>() {
//      @Override
//      public void onResponse(JSONObject response) {
//        int status = 0;
//        String message = "";
//        String token = "";
//        User user = new User();
//        try {
//          status = response.getInt("status");
//          message = response.getString("message");
//          token = response.getJSONObject("data").getString("token");
//          JSONObject userObj = response.getJSONObject("data").getJSONObject("user");
//          user = User.Parser.parse(userObj);
//          user.setToken(token);
//          onUserDetailReceivedComplete.onComplete(status, message, user);
//
//        } catch (JSONException e) {
//          e.printStackTrace();
//        }
//
//      }
//    }, new Response.ErrorListener() {
//      @Override
//      public void onErrorResponse(VolleyError error) {
//        onUserDetailReceivedComplete.onComplete(0, "", new User());
//      }
//    });
//
//    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    Volley.newRequestQueue(context).add(request);
//  }



  public void getUserFinance(final onFinanceReceivedComplete onFinanceReceivedComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.USER_FINANCE, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String token = "";
        Finance finance = new Finance();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          finance = Finance.Parser.parse(response.getJSONObject("data"));

          onFinanceReceivedComplete.onComplete(status, message, finance);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        onFinanceReceivedComplete.onComplete(0, "مشکل در ارتباط با سرور", new Finance());
      }
    }){
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





  public void getUserLastAddress(final onLastAddressReceived onLastAddressReceived){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.USER_LAST_ADDRESS, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String token = "";
        Address address;
        try {
          status = response.getInt("status");
          message = response.getString("message");
          if(status == 1){
            JSONObject data = response.getJSONObject("data");
            address = Address.Parser.parse(data);
          }else {
            address = null;
          }

          onLastAddressReceived.onComplete(status, message, address);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        onLastAddressReceived.onComplete(0, "مشکل در ارتباط با سرور", new Address());
      }
    }){
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










  public interface onUserDetailReceivedComplete{
    void onComplete(int status, String message, User user);
  }


  public interface onFinanceReceivedComplete{
    void onComplete(int status, String message, Finance finance);
  }

  public interface onLastAddressReceived{
    void onComplete(int status, String message, Address address);
  }


}
