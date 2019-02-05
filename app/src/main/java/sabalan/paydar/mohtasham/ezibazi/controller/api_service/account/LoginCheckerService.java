package sabalan.paydar.mohtasham.ezibazi.controller.api_service.account;

import android.content.Context;
import android.util.Log;

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

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.User;


public class LoginCheckerService {
  private Context context;
  public LoginCheckerService(Context context){
    this.context = context;
  }


  public void check(final User user, final onLoginCheckComplete onLoginCheckComplete){
    ApiRequest.getInstance(context).request(ApiRequest.GET, Urls.LOGIN_CHECK, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onLoginCheckComplete.onComplete(false, "");
          return;
        }
        boolean isLoggedIn = false;
        String full_name = "";
        try {
          if (status == 1){
            isLoggedIn = true;
            full_name = response.getJSONObject("data").getString("full_name");
          }else {
            isLoggedIn = false;
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onLoginCheckComplete.onComplete(isLoggedIn, full_name);
      }
    });

  }


  public interface onLoginCheckComplete{
    void onComplete(boolean isLoggedIn, String full_name);
  }


}
