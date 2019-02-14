package sabalan.paydar.mohtasham.ezibazi.controller.api_service.account;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.User;

public class AccountService {
  private Context context;
  public AccountService(Context context){
    this.context = context;
  }


  public void login(JSONObject jsonObject, final onLoginComplete onLoginComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.LOGIN, jsonObject, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if(error){
          onLoginComplete.onComplete(status, message, "", null);
          return;
        }
        String token = "";
        User user = new User();
          if(status == 1) {
            try {
              token = response.getJSONObject("data").getString("token");
              JSONObject userObj = response.getJSONObject("data").getJSONObject("user");
              user = User.Parser.parse(userObj);
              user.setToken(token);

              onLoginComplete.onComplete(status, message, token, user);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

      }
    });

  }



  public void register(JSONObject jsonObject, final onRegisterComplete onRegisterComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.REGISTER, jsonObject, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if(error){
          onRegisterComplete.onComplete(status, message, "", null);
          return;
        }

        String token = "";
        User user = new User();
        try {
          token = response.getJSONObject("data").getString("token");
          JSONObject userObj = response.getJSONObject("data").getJSONObject("user");
          user = User.Parser.parse(userObj);

          onRegisterComplete.onComplete(status, message, token, user);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });

  }



  public void logout(final onLogoutComplete onLogoutComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.LOGOUT, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onLogoutComplete.onComplete(status, message);
          return;
        }
        onLogoutComplete.onComplete(status, message);
      }
    });
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
