package ir.easybazi.system.hardware

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar

import ir.easybazi.system.application.G
import ir.easybazi.view.activity.ActivityNetworkError

//  public ConnectivityListener(View view, Snackbar snackbar) {
//    this.view = view;
//    this.snackbar = snackbar;
//  }
//
//  public ConnectivityListener(View view) {
//    this.view = view;
//  }

class ConnectivityListener : BroadcastReceiver() {


    private val snackbar: Snackbar? = null
    private val view: View? = null
    private var isShowNetworkError = false
    private lateinit var context: Context


    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected && networkInfo.isConnectedOrConnecting


        if (isConnected) {
            //      if(snackbar != null) {
            //        snackbar.dismiss();
            //      }

            if (G.mustReconnect) {
                G.mustReconnect = false
                val i = G.context.packageManager.getLaunchIntentForPackage(G.context.packageName)!!
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(i)
                isShowNetworkError = false

            }


        } else {

            G.mustReconnect = true
            if (!isShowNetworkError) {
                val intent1 = Intent(context, ActivityNetworkError::class.java)
                context.startActivity(intent1)
                isShowNetworkError = true
            }


            //        snackbar = Snackbar.make(view, "خطا در اتصال به سرور", Snackbar.LENGTH_INDEFINITE);
            //        snackbar.show();
        }
    }

}
