package ir.easybazi.view.activity

import android.app.FragmentTransaction
import android.content.IntentFilter
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.easybazi.R
import ir.easybazi.system.application.G
import ir.easybazi.system.hardware.ConnectivityListener
import ir.easybazi.system.hardware.Hardware
import ir.easybazi.system.pref_manager.UserPrefManager
import ir.easybazi.view.fragment.activity.FragmentActivity
import ir.easybazi.view.fragment.home.FragmentHome
import ir.easybazi.view.fragment.profile.FragmentProfile
import ir.easybazi.view.fragment.search.FragmentSearch

class ActivityMenu : AppCompatActivity() {

    internal lateinit var lyt_root: CoordinatorLayout
    internal var connectivityListener: ConnectivityListener? = null
    internal lateinit var lyt_bottom_buttons: LinearLayout
    internal lateinit var lyt_action: FrameLayout
    internal lateinit var img_profile: ImageView
    internal lateinit var img_search: ImageView
    internal lateinit var img_activity: ImageView
    internal lateinit var img_home: ImageView
    internal lateinit var bottom_nav: BottomNavigationView
    internal var ft: FragmentTransaction? = null

    private val frg_profile_num = 1
    private val frg_search_num = 2
    private val frg_activity_num = 3
    private val frg_home_num = 4

    internal var current_fragment_num: Int = 0


    internal val fragmentHome = FragmentHome()
    internal val fragmentActivity = FragmentActivity()
    internal val fragmentSearch = FragmentSearch()
    internal val fragmentProfile = FragmentProfile()

    internal lateinit var active: Fragment

    internal val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        instance = this

        //test crach
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))



        setupViews()


        val prefManager = UserPrefManager(this@ActivityMenu)

        //initialize home fragment as active
        initializeFragments()

        //    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottom_nav.getLayoutParams();
        //    layoutParams.setBehavior(new BottomNavigationViewBehavior());


        img_home.setOnClickListener { setFragment(frg_home_num) }

        img_activity.setOnClickListener { setFragment(frg_activity_num) }



        img_search.setOnClickListener { setFragment(frg_search_num) }


        img_profile.setOnClickListener { setFragment(frg_profile_num) }


    }


    private fun setupViews() {
        lyt_root = findViewById(R.id.lyt_root)
        lyt_action = findViewById(R.id.lyt_action)
        lyt_bottom_buttons = findViewById(R.id.lyt_bottom_buttons)
        bottom_nav = findViewById(R.id.bottom_nav)
        img_profile = findViewById(R.id.img_profile)
        img_search = findViewById(R.id.img_search)
        img_activity = findViewById(R.id.img_activity)
        img_home = findViewById(R.id.img_home)
    }

    private fun initializeFragments() {
        fm.beginTransaction().add(R.id.lyt_action, fragmentHome, "1").commit()
        fm.beginTransaction().add(R.id.lyt_action, fragmentActivity, "2").hide(fragmentActivity).commit()
        fm.beginTransaction().add(R.id.lyt_action, fragmentSearch, "3").hide(fragmentSearch).commit()
        fm.beginTransaction().add(R.id.lyt_action, fragmentProfile, "4").hide(fragmentProfile).commit()
        active = fragmentHome
        current_fragment_num = frg_home_num
        resetChoose(frg_home_num)
    }


    private fun setFragment(fragment_num: Int) {
        Hardware.hideKeyboard(this@ActivityMenu)

        when (fragment_num) {
            frg_home_num ->
                //        ft = getSupportFragmentManager().beginTransaction();
                //        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
                ////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
                //        ft.replace(R.id.lyt_action, fragmentHome);
                //        ft.commit();

                if (current_fragment_num != frg_home_num) {
                    fm.beginTransaction().hide(active).show(fragmentHome).commit()
                    active = fragmentHome
                    current_fragment_num = frg_home_num
                    resetChoose(fragment_num)
                }


            frg_activity_num ->
                //        ft = getSupportFragmentManager().beginTransaction();
                //        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
                ////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
                //        ft.replace(R.id.lyt_action, fragmentActivity);
                //        ft.commit();

                if (current_fragment_num != frg_activity_num) {
                    fm.beginTransaction().hide(active).show(fragmentActivity).commit()
                    active = fragmentActivity
                    current_fragment_num = frg_activity_num
                    resetChoose(fragment_num)
                }


            frg_search_num ->
                //        ft = getSupportFragmentManager().beginTransaction();
                //        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
                ////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
                //        ft.replace(R.id.lyt_action, new FragmentSearch());
                //        ft.commit();


                if (current_fragment_num != frg_search_num) {
                    fm.beginTransaction().hide(active).show(fragmentSearch).commit()
                    active = fragmentSearch
                    current_fragment_num = frg_search_num
                    resetChoose(fragment_num)
                }


            frg_profile_num ->
                //        ft = getSupportFragmentManager().beginTransaction();
                //        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
                ////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
                //        ft.replace(R.id.lyt_action, new FragmentProfile());
                //        ft.commit();


                if (current_fragment_num != frg_profile_num) {
                    fm.beginTransaction().hide(active).show(fragmentProfile).commit()
                    active = fragmentProfile
                    current_fragment_num = frg_profile_num
                    resetChoose(fragment_num)
                }
        }
    }


    private fun resetChoose(frg: Int) {
        img_profile.setImageResource(R.drawable.ic_person)
        img_search.setImageResource(R.drawable.ic_search)
        img_activity.setImageResource(R.drawable.ic_activity)
        img_home.setImageResource(R.drawable.ic_home)

        when (frg) {
            frg_profile_num -> img_profile.setImageResource(R.drawable.ic_person_ch)

            frg_search_num -> img_search.setImageResource(R.drawable.ic_search_ch)

            frg_activity_num -> img_activity.setImageResource(R.drawable.ic_activity_ch)

            frg_home_num -> img_home.setImageResource(R.drawable.ic_home_ch)
        }


    }


    override fun onStart() {
        super.onStart()

        //    connectivityListener = new ConnectivityListener(lyt_action);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(G.connectivityListener)
    }

    companion object {


        var instance: ActivityMenu? = null
            private set
    }

}
