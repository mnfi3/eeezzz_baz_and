package sabalan.paydar.mohtasham.ezibazi.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPrefManager {


  private static final String SETTING_SHARED_PREF_NAME = "setting_pref";
  private static final String IS_PLAY_VIDEOS = "is_play_videos";

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







}
