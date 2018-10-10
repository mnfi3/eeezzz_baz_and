package mohtasham.paydar.sabalan.ezbazi.controller.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.LoginCheckerService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.model.RentType;
import mohtasham.paydar.sabalan.ezbazi.model.User;


public class G extends Application {

  public static Context context;

    public static final String TAG=" G ";
  public static final String MAIN_URL="http://192.168.10.83/izi-bazi.ud/api";
//  public static final String MAIN_URL="http://ittiktak.com/ezibazi/public/api";
//  public static final String MAIN_URL="http://192.168.1.6/izi-bazi.ud/api";
  public static final String SALT="7c3d596ed03ab9116c547b0eb678b247";

  public static final String SALT_COPY="7c3d596ed03ab9116c547b0eb678b247";

  private static final String AES = "AES";

  public static  boolean isLoggedIn;
  public static List<RentType> rentTypes;


  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    initializeLogin();
    setFakeCity();
    getRentTypes();
  }

  private void setFakeCity(){
    new UserSharedPrefManager(context).saveCityId(329);
  }

  private static void getRentTypes(){
    RentService service = new RentService(context);
    service.getRentTypes(new RentService.onRentTypesReceived() {
      @Override
      public void onReceived(int status, String message, List<RentType> rentTypes) {
        if (status == 1){
          G.rentTypes = rentTypes;
        }
      }
    });
  }

  public static String getUserToken(){
    User user = getUser();
    if(user.getToken() == null){
      user.setToken("");
    }
    return user.getToken();
  }


  public static Context getActivityContext(AppCompatActivity activity){
    return activity.getApplicationContext();
  }

  public static void deleteCache(Context context) {
    try {
      File dir = context.getCacheDir();
      deleteDir(dir);
    } catch (Exception e) {}
  }

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
      return dir.delete();
    } else if(dir!= null && dir.isFile()) {
      return dir.delete();
    } else {
      return false;
    }
  }

  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = activity.getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(activity);
    }
    assert imm != null;
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

 /* public static String getDeviceId(Context context){
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getDeviceId();//IMEI
  }

  public static String getSubscriberId(Context context){
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getSubscriberId();//IMSI
  }*/

  public static String getDeviceModel(){
    //String myDeviceModel = android.os.Build.MODEL;
    return android.os.Build.MODEL;
  }

  private static String getWifiMac(){
    WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    return wm.getConnectionInfo().getMacAddress();
  }



  public static String getAndroidId(Context context){
    if(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).length()>0) {
      return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }else{
      return "";
    }
  }


  public static String getHashedString(String text){
    MessageDigest m = null;
    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    if (m != null) {
      m.reset();
    }
    if (m != null) {
      m.update(text.getBytes());
    }
    byte[] digest = new byte[0];
    if (m != null) {
      digest = m.digest();
    }
    BigInteger bigInt = new BigInteger(1,digest);
    String hashed_text = bigInt.toString(16);
    while(hashed_text.length() < 32 ){
      hashed_text = "0"+ hashed_text;
    }
    return hashed_text;
  }


  public static String getClientKey(){
    return G.getHashedString(
        G.SALT +
        G.getWifiMac() +
//        username +
        G.getDeviceModel()
       // G.getSubscriberId(G.context)
       // G.getAndroidId(G.context)
    );
  }



  public static String encrypt(String strClearText){
    try {
      SecretKey secretKey = generateKey();
//      for (int i = 0; i< Character.getNumericValue(getHashedString(SALT).charAt(10))-1; i++) {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(strClearText.getBytes("UTF-8"));
        strClearText= Base64.encodeToString(encrypted, Base64.DEFAULT);
//      }
      return strClearText;

    } catch (Exception e) {
      e.printStackTrace();
      return "not encrypted";
    }
  }



  public static String decrypt(String strEncrypted){
    try {
      SecretKey secretKey = generateKey();
//      for (int i = 0; i< Character.getNumericValue(getHashedString(SALT).charAt(10))-1; i++) {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT));
        strEncrypted= new String(decrypted, "UTF-8");
//      }
      return strEncrypted;
    } catch (Exception e) {
      e.printStackTrace();
      return "not decrypted";
    }
  }



  public static SecretKeySpec generateKey() {
//    byte[] ENCRYPTION_KEY = new byte[]{
//        '0',(byte)getHashedString(SALT).charAt(20),
//        '4',(byte)getHashedString(SALT).charAt(16),
//        '3',(byte)getHashedString(SALT).charAt(14),
//        'i',(byte)getHashedString(SALT).charAt(4) ,
//        'm',(byte)getHashedString(SALT).charAt(0) ,
//        'a',(byte)getHashedString(SALT).charAt(6) ,
//        'j',(byte)getHashedString(SALT).charAt(13),
//        'r',(byte)getHashedString(SALT).charAt(15),
//        'a',(byte)getHashedString(SALT).charAt(26),
//        'F',(byte)getHashedString(SALT).charAt(30),
//        'n',(byte)getHashedString(SALT).charAt(18),
//        'e',(byte)getHashedString(SALT).charAt(7) ,
//        's',(byte)getHashedString(SALT).charAt(2) ,
//        'h',(byte)getHashedString(SALT).charAt(25),
//        'o',(byte)getHashedString(SALT).charAt(30),
//        'M',(byte)getHashedString(SALT).charAt(29),
//      };
//
    byte[] ENCRYPTION_KEY = getHashedString(getClientKey()).getBytes();
    return new SecretKeySpec(ENCRYPTION_KEY, AES);
  }


  public static void initializeLogin(){
    loginCheck(new onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        G.isLoggedIn = isLoggedIn;
        if(isLoggedIn){
          UserSharedPrefManager prefManager = new UserSharedPrefManager(context);
          prefManager.saveUserFullName(user.getFull_name());
        }
      }
    });
  }


  public static void loginCheck(final onLoginCheck onLoginCheck){
    final User user = getUser();
    LoginCheckerService service = new LoginCheckerService(context);
    service.check(user, new LoginCheckerService.onLoginCheckComplete() {
      @Override
      public void onComplete(boolean isLoggedIn, String full_name) {
        user.setFull_name(full_name);
        onLoginCheck.onCheck(user, isLoggedIn);
        G.isLoggedIn = isLoggedIn;
      }
    });
  }


  public static User getUser(){
    UserSharedPrefManager prefManager = new UserSharedPrefManager(context);
    User user = prefManager.getUser();
    if(user.getFull_name() == null || user.getToken() == null){
      user.setFull_name("");
      user.setToken("");
    }
    return user;
  }







  public interface onLoginCheck{
    void onCheck(User user, boolean isLoggedIn);
  }

}