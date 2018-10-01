package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.activity.FragmentActivity;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.FragmentHome;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.profile.FragmentProfile;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.search.FragmentSearch;

public class ActivityMenu extends AppCompatActivity {


  ImageView img_profile, img_search, img_activity, img_home;
  BottomNavigationView bottom_nav;
  FragmentTransaction ft;

  private final int frg_profile_num = 1;
  private final int frg_search_num = 2;
  private final int frg_activity_num = 3;
  private final int frg_home_num = 4;

  int current_fragment_num;


  final FragmentHome fragmentHome = new FragmentHome();
  final FragmentActivity fragmentActivity = new FragmentActivity();
  final FragmentSearch fragmentSearch = new FragmentSearch();
  final FragmentProfile fragmentProfile = new FragmentProfile();

  Fragment active;

  final FragmentManager fm = getSupportFragmentManager();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    setupViews();

    //initialize home fragment as active
    initializeFragments();

//    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottom_nav.getLayoutParams();
//    layoutParams.setBehavior(new BottomNavigationViewBehavior());




    img_home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setFragment(frg_home_num);
      }
    });

    img_activity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setFragment(frg_activity_num);
      }
    });



    img_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setFragment(frg_search_num);
      }
    });


    img_profile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setFragment(frg_profile_num);
      }
    });








  }


  private void setupViews(){
    bottom_nav = findViewById(R.id.bottom_nav);
    img_profile = findViewById(R.id.img_profile);
    img_search = findViewById(R.id.img_search);
    img_activity = findViewById(R.id.img_activity);
    img_home = findViewById(R.id.img_home);
  }

  private void initializeFragments(){
    fm.beginTransaction().add(R.id.lyt_action, fragmentHome, "1").commit();
    fm.beginTransaction().add(R.id.lyt_action, fragmentActivity, "2").hide(fragmentActivity).commit();
    fm.beginTransaction().add(R.id.lyt_action,fragmentSearch, "3").hide(fragmentSearch).commit();
    fm.beginTransaction().add(R.id.lyt_action,fragmentProfile, "4").hide(fragmentProfile).commit();
    active = fragmentHome;
    current_fragment_num = 1;
    resetChoose(frg_home_num);
  }



  private void setFragment(int fragment_num){
    G.hideKeyboard(ActivityMenu.this);

    switch (fragment_num){
      case frg_home_num:
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//        ft.replace(R.id.lyt_action, fragmentHome);
//        ft.commit();

        if(current_fragment_num != frg_home_num){
          fm.beginTransaction().hide(active).show(fragmentHome).commit();
          active = fragmentHome;
          current_fragment_num = frg_home_num;
          resetChoose(fragment_num);
        }
        break;


      case frg_activity_num:
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//        ft.replace(R.id.lyt_action, fragmentActivity);
//        ft.commit();

        if(current_fragment_num != frg_activity_num){
          fm.beginTransaction().hide(active).show(fragmentActivity).commit();
          active = fragmentActivity;
          current_fragment_num = frg_activity_num;
          resetChoose(fragment_num);
        }

        break;





      case frg_search_num:
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//        ft.replace(R.id.lyt_action, new FragmentSearch());
//        ft.commit();


        if(current_fragment_num != frg_search_num){
          fm.beginTransaction().hide(active).show(fragmentSearch).commit();
          active = fragmentSearch;
          current_fragment_num = frg_search_num;
          resetChoose(fragment_num);
        }
        break;


      case frg_profile_num:
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
////        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//        ft.replace(R.id.lyt_action, new FragmentProfile());
//        ft.commit();


        if(current_fragment_num != frg_profile_num){
          fm.beginTransaction().hide(active).show(fragmentProfile).commit();
          active = fragmentProfile;
          current_fragment_num = frg_profile_num;
          resetChoose(fragment_num);
        }

        break;








    }
  }


  private void resetChoose(int frg){
    img_profile.setImageResource(R.drawable.ic_person);
    img_search.setImageResource(R.drawable.ic_search);
    img_activity.setImageResource(R.drawable.ic_activity);
    img_home.setImageResource(R.drawable.ic_home);

    switch (frg){
      case frg_profile_num:
        img_profile.setImageResource(R.drawable.ic_person_ch);
        break;

      case frg_search_num:
        img_search.setImageResource(R.drawable.ic_search_ch);
        break;

      case frg_activity_num:
        img_activity.setImageResource(R.drawable.ic_activity_ch);
        break;

      case frg_home_num:
        img_home.setImageResource(R.drawable.ic_home_ch);
        break;
    }


  }



}
