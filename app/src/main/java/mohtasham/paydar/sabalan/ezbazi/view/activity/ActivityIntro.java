package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.pref_manager.AppUsagePrefManager;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.intro.FragmentIntro1;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.intro.FragmentIntro2;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.intro.FragmentIntro3;


//create intro view for first time
public class ActivityIntro extends AppCompatActivity {

  Button btn_per, btn_next;

  int current_fragment = 1;
  FragmentTransaction ft;
  AppUsagePrefManager prefManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);

    prefManager = new AppUsagePrefManager(ActivityIntro.this);
    if (prefManager.getFirstUse() == 0){
      Intent intent = new Intent(ActivityIntro.this, ActivityMenu.class);
      startActivity(intent);
      finish();
    }

    setupViews();
    setTypeFace();

    btn_per.setVisibility(View.INVISIBLE);
    ft = getSupportFragmentManager().beginTransaction();
    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
    ft.replace(R.id.lyt_intro, new FragmentIntro1());
    ft.commit();


    btn_per.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Animation rotation = AnimationUtils.loadAnimation(ActivityIntro.this, R.anim.rotate);
        btn_per.startAnimation(rotation);
       if(current_fragment == 2){
         ft = getSupportFragmentManager().beginTransaction();
         ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
         btn_per.setVisibility(View.INVISIBLE);
         ft.replace(R.id.lyt_intro, new FragmentIntro1());
         ft.commit();
         current_fragment--;
       }else  if(current_fragment == 3){
         btn_next.setText("بعدی");
         ft = getSupportFragmentManager().beginTransaction();
         ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
         btn_per.setVisibility(View.VISIBLE);
         ft.replace(R.id.lyt_intro, new FragmentIntro2());
         ft.commit();
         current_fragment--;
       }
      }
    });

    btn_next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Animation rotation = AnimationUtils.loadAnimation(ActivityIntro.this, R.anim.rotate);
        btn_next.startAnimation(rotation);
        if(current_fragment == 1){
          ft = getSupportFragmentManager().beginTransaction();
          ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
          btn_per.setVisibility(View.VISIBLE);
          ft.replace(R.id.lyt_intro, new FragmentIntro2());
          ft.commit();
          current_fragment++;
        }else if(current_fragment == 2){
          ft = getSupportFragmentManager().beginTransaction();
          ft.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
          ft.replace(R.id.lyt_intro, new FragmentIntro3());
          ft.commit();
          btn_next.setText("تمام");
          current_fragment++;
        }
        else if(current_fragment == 3){
          prefManager = new AppUsagePrefManager(ActivityIntro.this);
          prefManager.saveFirstUse();
          Intent intent = new Intent(ActivityIntro.this, ActivityMenu.class);
          startActivity(intent);
          finish();
        }

      }
    });

  }



  private void setupViews(){
    btn_per = findViewById(R.id.btn_per);
    btn_next = findViewById(R.id.btn_next);
  }

  private void setTypeFace(){
    btn_per.setTypeface(MyViews.getIranSansFont(ActivityIntro.this));
    btn_next.setTypeface(MyViews.getIranSansLightFont(ActivityIntro.this));
  }
}
