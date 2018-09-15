package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.layout_behavior.BottomNavigationViewBehavior;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.activity.FragmentActivity;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.FragmentHome;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.profile.FragmentProfile;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.search.FragmentSearch;

public class ActivityMenu extends AppCompatActivity {


  ImageView img_profile, img_search, img_activity, img_home;
  BottomNavigationView bottom_nav;
  FragmentTransaction ft;

  private final int frgProfile = 1;
  private final int frgSearch = 2;
  private final int frgActivity = 3;
  private final int frgHome = 4;

  int current_fragment;



  FragmentHome fragmentHome = new FragmentHome();
  FragmentActivity fragmentActivity = new FragmentActivity();
//  FragmentHome fragmentHome = new FragmentHome();;
//  FragmentHome fragmentHome = new FragmentHome();;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    setupViews();

//    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottom_nav.getLayoutParams();
//    layoutParams.setBehavior(new BottomNavigationViewBehavior());



    //initialize first time
    setFragment(frgHome);
    resetChoose(frgHome);







    img_profile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetChoose(frgProfile);
        if(current_fragment == frgProfile){
          setFragment(frgProfile);
        }else {
//          restoreFragment(frgProfile);
          setFragment(frgProfile);
        }
      }
    });



    img_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetChoose(frgSearch);
        if(current_fragment == frgSearch){
          setFragment(frgSearch);
        }else {
//          restoreFragment(frgSearch);
          setFragment(frgSearch);
        }
      }
    });

    img_activity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetChoose(frgActivity);
        if(current_fragment == frgActivity){
          setFragment(frgActivity);
        }else {
          setFragment(frgActivity);
//          restoreFragment(frgActivity);
        }
      }
    });

    img_home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetChoose(frgHome);
        if(current_fragment == frgHome){
          setFragment(frgHome);
        }else {
          setFragment(frgHome);
//          restoreFragment(frgHome);
        }
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



  private void setFragment(int fragment){
    switch (fragment){
      case frgProfile :
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
//        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        ft.replace(R.id.lyt_action, new FragmentProfile());
        ft.commit();
        current_fragment = frgProfile;

        break;



      case frgSearch:
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
//        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        ft.replace(R.id.lyt_action, new FragmentSearch());
        ft.commit();
        current_fragment = frgSearch;


        break;



      case frgActivity:
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
//        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        ft.replace(R.id.lyt_action, fragmentActivity);
        ft.commit();
        current_fragment = frgActivity;

        break;



      case frgHome:
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_fragment_enter_from_right, R.anim.anim_fragment_exit_to_left);
//        ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        ft.replace(R.id.lyt_action, fragmentHome);
        ft.commit();
        current_fragment = frgHome;
        break;
    }
  }



  private void restoreFragment(int fragment){
    switch (fragment){
      case frgProfile :

        break;



      case frgSearch:

        break;



      case frgActivity:

        break;



      case frgHome:

        break;
    }
  }




  private void resetChoose(int frg){
//    img_profile.setAlpha(0.6f);
    img_profile.setImageResource(R.drawable.ic_person);

//    img_search.setAlpha(0.6f);
    img_search.setImageResource(R.drawable.ic_search);

//    img_activity.setAlpha(0.6f);
    img_activity.setImageResource(R.drawable.ic_activity);

//    img_home.setAlpha(0.6f);
    img_home.setImageResource(R.drawable.ic_home);

    switch (frg){
      case frgProfile :
//        img_profile.setAlpha(1f);
        img_profile.setImageResource(R.drawable.ic_person_ch);
        break;

      case frgSearch :
//        img_search.setAlpha(1f);
        img_search.setImageResource(R.drawable.ic_search_ch);
        break;


      case frgActivity :
//        img_activity.setAlpha(1f);
        img_activity.setImageResource(R.drawable.ic_activity_ch);
        break;


      case frgHome :
//        img_home.setAlpha(1f);
        img_home.setImageResource(R.drawable.ic_home_ch);
        break;
    }


  }



}
