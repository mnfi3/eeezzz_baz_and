package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;

public class ActivityHome extends AppCompatActivity {

  private static final String TAG = "ActivityMain";

  ImageView img_open_navigation;
  DrawerLayout drawerLayout;
  NavigationView navigation;
  FrameLayout lyt_navigation;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);



      //Log.i(TAG, "onCreate: user_token = " + new UserSharedPrefManager(this).getUser().getToken());



      setupViews();

//    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
//    ft.replace(R.id.lyt_navigation, new FragmentNavigation());
//    ft.replace(R.id.lyt_main_shops, new FragmentRents());
//    ft.replace(R.id.lyt_main_rents, new FragmentShops());
//    ft.replace(R.id.lyt_main_posts, new FragmentPosts());
//    ft.commit();

//    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//    ft2.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//    ft2.commit();


//    img_open_navigation.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        drawerLayout.openDrawer(Gravity.RIGHT);
//      }
//    });





  }

//       drawerLayout.closeDrawers();
  private void setupViews(){
//    img_open_navigation = (ImageView) findViewById(R.id.img_open_navigation);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//    navigation = (NavigationView) findViewById(R.id.navigation);
//    lyt_navigation = (FrameLayout) findViewById(R.id.lyt_navigation);
  }
}
