package sabalan.paydar.mohtasham.ezibazi.system.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;


import sabalan.paydar.mohtasham.ezibazi.system.application.Crypt;
import sabalan.paydar.mohtasham.ezibazi.model.User;

public class UserPrefManager {

  private static final String TAG = "UserSharedPrefManager";


  private static final String USER_SHARED_PREF_NAME = "user_shared_pref";
  private static final String FULL_NAME = "name";
  private static final String TOKEN = "token";

//  private static final String CLIENT_KEY = "client_key";

  private SharedPreferences sharedPreferences;

  public UserPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void saveUser(User user) {
    if (user != null) {
      String newToken = Crypt.encrypt(user.getToken());
      user.setToken(newToken);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(FULL_NAME, user.getFull_name());
      editor.putString(TOKEN, user.getToken());
//      editor.putString(CLIENT_KEY, user.getClient_key());
      editor.apply();
    }
  }

  public void saveUserFullName(String f_name){
    String full_name = "";
    if (f_name != null){
      full_name = f_name;
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(FULL_NAME, full_name);
      editor.apply();
    }


  }

  public User getUser() {
    User user = new User();
    user.setFull_name(sharedPreferences.getString(FULL_NAME, ""));
    String main_token = Crypt.decrypt(sharedPreferences.getString(TOKEN ,""));
    user.setToken(main_token);
//    user.setClient_key(sharedPreferences.getString(CLIENT_KEY , ""));
    return user;
  }






}
