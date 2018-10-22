package sabalan.paydar.mohtasham.ezibazi.controller.system.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;

import sabalan.paydar.mohtasham.ezibazi.BuildConfig;

import io.fabric.sdk.android.Fabric;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.auth.Auth;
import sabalan.paydar.mohtasham.ezibazi.controller.system.hardware.ConnectivityListener;
import sabalan.paydar.mohtasham.ezibazi.controller.system.hardware.Storage;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.CityPrefManager;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.UserPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;
import sabalan.paydar.mohtasham.ezibazi.model.User;


public class G extends Application {

//  getString(R.string.default_notification_channel_id);

  @SuppressLint("StaticFieldLeak")
  public static Context context;
  public static final String TAG=" G ";

  public static ConnectivityListener connectivityListener ;
  public static boolean mustReconnect = false;


  public static  boolean isLoggedIn = false;
  public static List<RentType> rentTypes;

  //analytics
  private FirebaseAnalytics mFirebaseAnalytics;


  @Override
  public void onCreate() {
    super.onCreate();
//    Crashlytics crashlyticsKit = new Crashlytics.Builder()
//      .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//      .build();

// Initialize Fabric with the debug-disabled crashlytics.
//    Fabric.with(this, crashlyticsKit);
    Fabric.with(this, new Crashlytics());
    Fabric.with(this, new Answers());


    context = getApplicationContext();
    initializeLogin();
    setFakeCity();
    getRentTypes();
    Storage.deleteCache(context);
    connectivityListener = new ConnectivityListener();
    mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    appOpenAnalytics();
  }

  @SuppressLint("InvalidAnalyticsName")
  private void appOpenAnalytics(){
    Bundle bundle = new Bundle();
    bundle.putString("APP_OPEN", "1");
    mFirebaseAnalytics.logEvent("App", bundle);
  }

  private void setFakeCity(){
//    new CityPrefManager(context).setCityId(AppConfig.DEFAULT_CITY_ID);
    new CityPrefManager(context).setCityId(329);
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



  public static void initializeLogin(){
    Auth.loginCheck( new onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        G.isLoggedIn = isLoggedIn;
        if(isLoggedIn){
          UserPrefManager prefManager = new UserPrefManager(context);
          prefManager.saveUserFullName(user.getFull_name());
        }
      }
    });
  }


  public interface onLoginCheck{
    void onCheck(User user, boolean isLoggedIn);
  }

}