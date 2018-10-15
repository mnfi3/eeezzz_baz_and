package sabalan.paydar.mohtasham.ezibazi.controller.system.hardware;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;

public class Hardware {

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

  @SuppressLint("HardwareIds")
  public static String getWifiMac(){
    WifiManager wm = (WifiManager) G.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    assert wm != null;
    return wm.getConnectionInfo().getMacAddress();
  }



  @SuppressLint("HardwareIds")
  public static String getAndroidId(Context context){
    if(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).length()>0) {
      return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }else{
      return "";
    }
  }
}
