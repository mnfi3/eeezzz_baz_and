package sabalan.paydar.mohtasham.ezibazi.api_service.firebase;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Device;

public class FireBaseApiService {
  private Context context;
  public FireBaseApiService(Context context){
    this.context = context;
  }




  public void refreshFcmToken(JSONObject object, final onRefreshTokenReceived onRefreshTokenReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.REFRESH_FCM_TOKEN, object, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRefreshTokenReceived.onReceived(status, message, new Device());
          return;
        }

        Device device;
        try {
          status = response.getInt("status");
          message = response.getString("message");
          device = Device.Parser.parse(response.getJSONObject("data"));
          onRefreshTokenReceived.onReceived(status, message, device);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }







  public interface onRefreshTokenReceived{
    void onReceived(int status, String message, Device device);
  }


}
