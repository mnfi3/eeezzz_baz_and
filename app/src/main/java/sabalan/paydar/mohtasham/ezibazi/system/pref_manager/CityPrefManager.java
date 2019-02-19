package sabalan.paydar.mohtasham.ezibazi.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;

import sabalan.paydar.mohtasham.ezibazi.system.config.AppConfig;

public class CityPrefManager {

  private static final String CITY_SHARED_PREF_NAME = "city_shared_pref";
  private static final String CITY_ID = "city_id";
//  private static final String CLIENT_KEY = "client_key";

  private SharedPreferences sharedPreferences;

  public CityPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(CITY_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void setCityId(int id){
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(CITY_ID, id);
    editor.apply();
  }

  public int getCityId(){
    return sharedPreferences.getInt(CITY_ID, AppConfig.DEFAULT_CITY_ID);
  }







}
