package sabalan.paydar.mohtasham.ezibazi.controller.system.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityNetworkError;

public class ConnectivityListener extends BroadcastReceiver {




  private Snackbar snackbar;
  private View view;
  private boolean isShowNetworkError = false;

//  public ConnectivityListener(View view, Snackbar snackbar) {
//    this.view = view;
//    this.snackbar = snackbar;
//  }
//
//  public ConnectivityListener(View view) {
//    this.view = view;
//  }

  public ConnectivityListener(){

  }


  @Override
  public void onReceive(Context context, Intent intent) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    assert connectivityManager != null;
    NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
    boolean isConnected = networkInfo!=null && networkInfo.isConnected();


    if(isConnected){
//      if(snackbar != null) {
//        snackbar.dismiss();
//      }

      if (G.mustReconnect){
        G.mustReconnect = false;
        Intent i = G.context.getPackageManager().getLaunchIntentForPackage( G.context.getPackageName() );
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        isShowNetworkError = false;

      }







    }else{

        G.mustReconnect = true;
        if(!isShowNetworkError) {
          Intent intent1 = new Intent(context, ActivityNetworkError.class);
          context.startActivity(intent1);
          isShowNetworkError = true;
        }



//        snackbar = Snackbar.make(view, "خطا در اتصال به سرور", Snackbar.LENGTH_INDEFINITE);
//        snackbar.show();
    }
  }
  
}
