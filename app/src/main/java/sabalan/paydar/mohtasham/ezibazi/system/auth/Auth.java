package sabalan.paydar.mohtasham.ezibazi.system.auth;

import sabalan.paydar.mohtasham.ezibazi.api_service.account.LoginCheckerService;
import sabalan.paydar.mohtasham.ezibazi.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.UserPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.User;

public class Auth {




  public static String getUserToken(){
    User user = getUser();
    if(user.getToken() == null){
      user.setToken("");
    }
    return user.getToken();
  }


  private static User getUser(){
    UserPrefManager prefManager = new UserPrefManager(G.context);
    User user = prefManager.getUser();
    if(user.getFull_name() == null || user.getToken() == null){
      user.setFull_name("");
      user.setToken("");
    }
    return user;
  }

  public static void loginCheck(final G.onLoginCheck onLoginCheck){
    final User user = Auth.getUser();
    LoginCheckerService service = new LoginCheckerService(G.context);
    service.check(user, new LoginCheckerService.onLoginCheckComplete() {
      @Override
      public void onComplete(boolean isLoggedIn, String full_name) {
        user.setFull_name(full_name);
        onLoginCheck.onCheck(user, isLoggedIn);
        G.isLoggedIn = isLoggedIn;
      }
    });
  }
}
