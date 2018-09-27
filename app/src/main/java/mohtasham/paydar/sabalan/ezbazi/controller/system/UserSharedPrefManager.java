package mohtasham.paydar.sabalan.ezbazi.controller.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import mohtasham.paydar.sabalan.ezbazi.model.User;

public class UserSharedPrefManager {

  private static final String TAG = "UserSharedPrefManager";


  private static final String USER_SHARED_PREF_NAME = "user_shared_pref";
  private static final String USER_NAME = "user_name";
  private static final String TOKEN = "token";
  private static final String CITY_ID = "city_id";
//  private static final String CLIENT_KEY = "client_key";

  private SharedPreferences sharedPreferences;

  public UserSharedPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void saveUser(User user) {
    if (user != null) {
      String newToken = G.encrypt(user.getToken());
      user.setToken(newToken);
      Log.i(TAG, "saveUser: new_token = " + newToken);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(USER_NAME, user.getUser_name());
      editor.putString(TOKEN, user.getToken());
//      editor.putString(CLIENT_KEY, user.getClient_key());
      editor.apply();
    }
  }

  public User getUser() {
    User user = new User();
    user.setUser_name(sharedPreferences.getString(USER_NAME, ""));
    String main_token = G.decrypt(sharedPreferences.getString(TOKEN ,""));
    user.setToken(main_token);
//    user.setClient_key(sharedPreferences.getString(CLIENT_KEY , ""));
    return user;
  }


  public void saveCityId(int id){
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(CITY_ID, id);
    editor.apply();
  }

  public int getCityId(){
    return sharedPreferences.getInt(CITY_ID, 0);
  }

}
