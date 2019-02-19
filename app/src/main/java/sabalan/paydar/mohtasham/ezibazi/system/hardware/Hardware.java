package sabalan.paydar.mohtasham.ezibazi.system.hardware;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.UUID;

import sabalan.paydar.mohtasham.ezibazi.system.application.G;

public class Hardware {

  public static TelephonyManager telephonyManager;


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

  @SuppressLint("HardwareIds")
  public static String getDeviceId() {
    if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return "";
    }
    telephonyManager = (TelephonyManager) G.context.getSystemService(Context.TELEPHONY_SERVICE);
    assert telephonyManager != null;
    return telephonyManager.getDeviceId();//IMEI
  }

  @SuppressLint("HardwareIds")
  public static String getSubscriberId() {
    if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return "";
    }
    telephonyManager = (TelephonyManager) G.context.getSystemService(Context.TELEPHONY_SERVICE);
    assert telephonyManager != null;
    return telephonyManager.getSubscriberId();//IMSI
  }

  public static String getDeviceModel() {
    //String myDeviceModel = android.os.Build.MODEL;
    return android.os.Build.MODEL;
  }

  public static String getDeviceBrand() {
    return Build.BRAND;
  }

  public static String getDevice() {
    return Build.DEVICE;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public static String getSerial() {
    if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return "";
    }
    return Build.getSerial();
  }


  @SuppressLint("HardwareIds")
  public static String getWifiMac(){
    WifiManager wm = (WifiManager) G.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    assert wm != null;
    return wm.getConnectionInfo().getMacAddress();
  }


//  @SuppressLint("HardwareIds")
//  public static String getBluetoothInfo(){
//    BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//    return ba.getAddress();
//  }



  @SuppressLint("HardwareIds")
  public static String getAndroidId(){
    if(Settings.Secure.getString(G.context.getContentResolver(), Settings.Secure.ANDROID_ID).length()>0) {
      return Settings.Secure.getString(G.context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }else{
      return "";
    }
  }



  //not unique
  public static String getUUID(){
    return UUID.randomUUID().toString();
  }


}
