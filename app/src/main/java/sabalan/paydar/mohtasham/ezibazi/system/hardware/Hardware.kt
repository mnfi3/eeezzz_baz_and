package sabalan.paydar.mohtasham.ezibazi.system.hardware

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.view.View
import android.view.inputmethod.InputMethodManager

import java.util.UUID

import sabalan.paydar.mohtasham.ezibazi.system.application.G

object Hardware {

    var telephonyManager: TelephonyManager? = null

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    //IMEI
    val deviceId: String
        @SuppressLint("HardwareIds")
        get() {
            if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ""
            }
            telephonyManager = G.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            assert(telephonyManager != null)
            return telephonyManager!!.deviceId
        }

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    //IMSI
    val subscriberId: String
        @SuppressLint("HardwareIds")
        get() {
            if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ""
            }
            telephonyManager = G.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            assert(telephonyManager != null)
            return telephonyManager!!.subscriberId
        }

    //String myDeviceModel = android.os.Build.MODEL;
    val deviceModel: String
        get() = android.os.Build.MODEL

    val deviceBrand: String
        get() = Build.BRAND

    val device: String
        get() = Build.DEVICE

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    val serial: String
        @RequiresApi(api = Build.VERSION_CODES.O)
        get() = if (ActivityCompat.checkSelfPermission(G.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ""
        } else Build.getSerial()


    val wifiMac: String
        @SuppressLint("HardwareIds")
        get() {
            val wm = G.context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wm.connectionInfo.macAddress
        }


    //  @SuppressLint("HardwareIds")
    //  public static String getBluetoothInfo(){
    //    BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    //    return ba.getAddress();
    //  }


    val androidId: String
        @SuppressLint("HardwareIds")
        get() = if (Settings.Secure.getString(G.context.contentResolver, Settings.Secure.ANDROID_ID).length > 0) {
            Settings.Secure.getString(G.context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            ""
        }


    //not unique
    val uuid: String
        get() = UUID.randomUUID().toString()


    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        assert(imm != null)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
