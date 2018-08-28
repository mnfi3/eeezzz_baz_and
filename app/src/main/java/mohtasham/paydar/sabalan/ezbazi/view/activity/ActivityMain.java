package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu.FragmentNavigation;

public class ActivityMain extends AppCompatActivity {

  ImageView img_open_navigation;
  DrawerLayout drawerLayout;
  NavigationView navigation;
  FrameLayout lyt_navigation;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    setupViews();

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.lyt_navigation, new FragmentNavigation());
    ft.commit();


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
