package sabalan.paydar.mohtasham.ezibazi.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;

import sabalan.paydar.mohtasham.ezibazi.system.application.Crypt;

public class SettingPrefManager {


  private static final String SETTING_SHARED_PREF_NAME = "setting_pref";
  private static final String IS_PLAY_VIDEOS = "is_play_videos";
  private static final String FCM_CLIENT_KEY = "fcm_client_key";

  private SharedPreferences sharedPreferences;

  public SettingPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(SETTING_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }




  public int getPlayVideos() {
    return sharedPreferences.getInt(IS_PLAY_VIDEOS, 2);
  }

  public void savePlayVideo(int is_play){
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(IS_PLAY_VIDEOS, is_play);
    editor.apply();
  }


  public void saveFcmClientKey(){
    String client_key = Crypt.generateFcmClientKey();
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(FCM_CLIENT_KEY, client_key);
    editor.apply();
  }

  public String getFcmClientKey(){
    return sharedPreferences.getString(FCM_CLIENT_KEY, "");
  }







}
