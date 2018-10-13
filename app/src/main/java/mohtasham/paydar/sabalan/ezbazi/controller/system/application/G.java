package mohtasham.paydar.sabalan.ezbazi.controller.system.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import java.util.List;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.auth.Auth;
import mohtasham.paydar.sabalan.ezbazi.controller.system.hardware.ConnectivityListener;
import mohtasham.paydar.sabalan.ezbazi.controller.system.hardware.Storage;
import mohtasham.paydar.sabalan.ezbazi.controller.system.pref_manager.CityPrefManager;
import mohtasham.paydar.sabalan.ezbazi.controller.system.pref_manager.UserPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.RentType;
import mohtasham.paydar.sabalan.ezbazi.model.User;


public class G extends Application {

  @SuppressLint("StaticFieldLeak")
  public static Context context;
  public static final String TAG=" G ";

  public static ConnectivityListener connectivityListener ;
  public static boolean mustReconnect = false;


  public static  boolean isLoggedIn = true;
  public static List<RentType> rentTypes;


  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    initializeLogin();
    setFakeCity();
    getRentTypes();
    Storage.deleteCache(context);
    connectivityListener = new ConnectivityListener();
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