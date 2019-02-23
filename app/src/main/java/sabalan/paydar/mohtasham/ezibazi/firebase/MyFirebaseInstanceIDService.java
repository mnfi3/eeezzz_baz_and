package sabalan.paydar.mohtasham.ezibazi.firebase;

import android.annotation.SuppressLint;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import sabalan.paydar.mohtasham.ezibazi.api_service.firebase.FireBaseApiService;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.FcmPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.Device;
import sabalan.paydar.mohtasham.ezibazi.model.Fcm;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


  private static final String TAG = "MyFirebaseInstanceIDService";




  @SuppressLint("LongLogTag")
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//    Log.d(TAG, "Refreshed token: " + refreshedToken);
    Fcm fcm = new Fcm();
    fcm.setToken(refreshedToken);
    fcm.setClient_key(new SettingPrefManager(MyFirebaseInstanceIDService.this).getFcmClientKey());
    FcmPrefManager fcmPrefManager = new FcmPrefManager(MyFirebaseInstanceIDService.this);
    fcmPrefManager.saveFcm(fcm);
    sendFcmInfoToServer(fcm);
  }


  private void sendFcmInfoToServer(Fcm fcm){
//    Fcm fcm = new FcmPrefManager(MyFirebaseInstanceIDService.this).getFcm();
    JSONObject object = new JSONObject();
    try {
      object.put("device_type", "ANDROID");
      object.put("fcm_token", fcm.getToken());
      object.put("client_key", fcm.getClient_key());
    } catch (JSONException e) {
    }

    FireBaseApiService service = new FireBaseApiService(MyFirebaseInstanceIDService.this);
    service.refreshFcmToken(object, new FireBaseApiService.onRefreshTokenReceived() {
      @Override
      public void onReceived(int status, String message, Device device) {
      }
    });
  }

}