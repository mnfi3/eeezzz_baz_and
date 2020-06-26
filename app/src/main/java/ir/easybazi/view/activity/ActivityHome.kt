package ir.easybazi.view.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

import ir.easybazi.R

class ActivityHome : AppCompatActivity() {

    internal var img_open_navigation: ImageView? = null
    internal lateinit var drawerLayout: DrawerLayout
    internal var navigation: NavigationView? = null
    internal var lyt_navigation: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        //Log.i(TAG, "onCreate: user_token = " + new UserSharedPrefManager(this).getUser().getToken());


        setupViews()

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
    private fun setupViews() {
        //    img_open_navigation = (ImageView) findViewById(R.id.img_open_navigation);
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        //    navigation = (NavigationView) findViewById(R.id.navigation);
        //    lyt_navigation = (FrameLayout) findViewById(R.id.lyt_navigation);
    }

    companion object {

        private val TAG = "ActivityHome"
    }
}
