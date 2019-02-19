package sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;

import sabalan.paydar.mohtasham.ezibazi.controller.system.application.Crypt;
import sabalan.paydar.mohtasham.ezibazi.model.Fcm;

public class FcmPrefManager {

  private static final String TAG = "FcmPrefManager";


  private static final String USER_SHARED_PREF_NAME = "fcm_shared_pref";
  private static final String FCM_TOKEN = "fcm_token";
  private static final String FCM_CLIENT_KEY = "fcm_client_key";


  private SharedPreferences sharedPreferences;

  public FcmPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void saveFcm(Fcm fcm) {
    String newToken = Crypt.encrypt(fcm.getToken());
    String newClientKey = Crypt.encrypt(fcm.getClient_key());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(FCM_TOKEN, newToken);
    editor.putString(FCM_CLIENT_KEY, newClientKey);
    editor.apply();
  }



  public Fcm getFcm() {
    Fcm fcm = new Fcm();
    String token = Crypt.decrypt(sharedPreferences.getString(FCM_TOKEN,""));
    String client_key = Crypt.decrypt(sharedPreferences.getString(FCM_CLIENT_KEY,""));
    fcm.setToken(token);
    fcm.setClient_key(client_key);
    return fcm;
  }






}
