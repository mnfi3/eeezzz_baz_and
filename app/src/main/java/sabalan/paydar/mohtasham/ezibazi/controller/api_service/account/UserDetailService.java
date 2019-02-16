package sabalan.paydar.mohtasham.ezibazi.controller.api_service.account;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Address;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
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
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.USER_FINANCE, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onFinanceReceivedComplete.onComplete(status, message, new Finance());
          return;
        }

        Finance finance = new Finance();
        try {
          finance = Finance.Parser.parse(response.getJSONObject("data"));
          onFinanceReceivedComplete.onComplete(status, message, finance);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }





  public void getUserLastAddress(final onLastAddressReceived onLastAddressReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.USER_LAST_ADDRESS, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if(error){
          onLastAddressReceived.onComplete(status, message,new Address());
          return;
        }

        Address address = null;
        if (status == 1){
          try {
            address = Address.Parser.parse(response.getJSONObject("data"));
            onLastAddressReceived.onComplete(status, message, address);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

      }
    });


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
