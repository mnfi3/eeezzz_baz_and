package sabalan.paydar.mohtasham.ezibazi.api_service.account;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.User;


public class LoginCheckerService {
  private Context context;
  public LoginCheckerService(Context context){
    this.context = context;
  }


  public void check(final User user, final onLoginCheckComplete onLoginCheckComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.LOGIN_CHECK, null, true, new ApiRequest.onDataReceived() {
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

          onLoginCheckComplete.onComplete(isLoggedIn, full_name);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

  }


  public interface onLoginCheckComplete{
    void onComplete(boolean isLoggedIn, String full_name);
  }


}
