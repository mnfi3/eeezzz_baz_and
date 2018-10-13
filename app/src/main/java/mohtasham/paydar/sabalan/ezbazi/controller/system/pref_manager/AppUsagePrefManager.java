package mohtasham.paydar.sabalan.ezbazi.controller.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;
public class AppUsagePrefManager {


  private static final String APP_USAGE_SHARED_PREF_NAME = "app_usage_pref";
  private static final String IS_FIRST_USE = "is_first_use";
//  private static final String CLIENT_KEY = "client_key";

  private SharedPreferences sharedPreferences;

  public AppUsagePrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(APP_USAGE_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }




  public int getFirstUse() {
    int first_use = sharedPreferences.getInt(IS_FIRST_USE, 1);
    if(first_use == 1){
      return 1;
    }else {
      return 0;
    }
  }

  public void saveFirstUse(){
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(IS_FIRST_USE, 0);
    editor.apply();
  }







}
