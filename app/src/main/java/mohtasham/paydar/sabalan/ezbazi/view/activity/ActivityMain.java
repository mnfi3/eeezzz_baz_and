package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu.FragmentNavigation;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu.FragmentPosts;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu.FragmentRents;

public class ActivityMain extends AppCompatActivity {

  private static final String TAG = "ActivityMain";

  ImageView img_open_navigation;
  DrawerLayout drawerLayout;
  NavigationView navigation;
  FrameLayout lyt_navigation;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);



      //Log.i(TAG, "onCreate: user_token = " + new UserSharedPrefManager(this).getUser().getToken());

      G.loginCheck(new G.onLoginCheck() {
        @Override
        public void onCheck(User user, boolean isLoggedIn) {
          Log.i(TAG, "onCheck: user_name = " + user.getUser_name() + "is_logged_in = " + isLoggedIn);
        }
      });

      setupViews();

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.lyt_navigation, new FragmentNavigation());
    ft.replace(R.id.lyt_main_rents, new FragmentRents());
    ft.replace(R.id.lyt_main_posts, new FragmentPosts());
    ft.commit();

//    ft.commit();


    img_open_navigation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawerLayout.openDrawer(Gravity.RIGHT);
      }
    });





  }

//       drawerLayout.closeDrawers();
  private void setupViews(){
    img_open_navigation = (ImageView) findViewById(R.id.img_open_navigation);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    navigation = (NavigationView) findViewById(R.id.navigation);
    lyt_navigation = (FrameLayout) findViewById(R.id.lyt_navigation);
  }
}
