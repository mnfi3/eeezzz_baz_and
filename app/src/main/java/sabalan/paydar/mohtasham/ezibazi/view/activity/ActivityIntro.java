package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.AppUsagePrefManager;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.layout_behavior.OnSwipeTouchListener;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro1;
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro2;
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro3;


//create intro view for first time
public class ActivityIntro extends AppCompatActivity {

  Button btn_per, btn_next;

  int current_fragment = 1;
  FragmentTransaction ft;
  AppUsagePrefManager prefManager;
  FragmentIntro1 intro1;
  FragmentIntro2 intro2;
  FragmentIntro3 intro3;

  RelativeLayout lyt_root;
  FrameLayout lyt_intro;

  final FragmentManager fm = getSupportFragmentManager();

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);

    intro1 = new FragmentIntro1();
    intro2 = new FragmentIntro2();
    intro3 = new FragmentIntro3();

    prefManager = new AppUsagePrefManager(ActivityIntro.this);
    if (prefManager.getFirstUse() == 0){
      Intent intent = new Intent(ActivityIntro.this, ActivityMenu.class);
      startActivity(intent);
      finish();
    }

    SettingPrefManager prefManager = new SettingPrefManager(ActivityIntro.this);
    prefManager.saveFcmClientKey();

    setupViews();
    setTypeFace();
    initializeFragments();

    btn_per.setVisibility(View.INVISIBLE);

    btn_per.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        prev();
      }
    });

    btn_next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        next();
      }
    });

    lyt_root.setOnTouchListener(new OnSwipeTouchListener(ActivityIntro.this) {
      public void onSwipeTop() {
//        Toast.makeText(ActivityIntro.this, "top", Toast.LENGTH_SHORT).show();
      }
      public void onSwipeRight() {
        prev();
//        Toast.makeText(ActivityIntro.this, "right", Toast.LENGTH_SHORT).show();
      }
      public void onSwipeLeft() {
        next();
//        Toast.makeText(ActivityIntro.this, "left", Toast.LENGTH_SHORT).show();
      }
      public void onSwipeBottom() {
//        Toast.makeText(ActivityIntro.this, "bottom", Toast.LENGTH_SHORT).show();
      }

    });



  }



  private void setupViews(){
    btn_per = findViewById(R.id.btn_per);
    btn_next = findViewById(R.id.btn_next);
    lyt_root = findViewById(R.id.lyt_root);
    lyt_intro = findViewById(R.id.lyt_intro);
  }

  private void setTypeFace(){
    btn_per.setTypeface(MyViews.getIranSansFont(ActivityIntro.this));
    btn_next.setTypeface(MyViews.getIranSansLightFont(ActivityIntro.this));
  }


  private void initializeFragments(){
    fm.beginTransaction().add(R.id.lyt_intro, intro1, "1").commit();
    fm.beginTransaction().add(R.id.lyt_intro, intro2, "2").hide(intro2).commit();
    fm.beginTransaction().add(R.id.lyt_intro, intro3, "3").hide(intro3).commit();
  }


  private void setButtonsAnim(boolean is_next){
    if(!is_next) {
      Animation rotation = AnimationUtils.loadAnimation(ActivityIntro.this, R.anim.rotate_intro_button2);
      btn_per.startAnimation(rotation);
    }else {
      Animation rotation2 = AnimationUtils.loadAnimation(ActivityIntro.this, R.anim.rotate_intro_button);
      btn_next.startAnimation(rotation2);
    }
  }

  private void prev(){
    setButtonsAnim(false);
    if(current_fragment == 2){
      fm.beginTransaction().hide(intro2).show(intro1).commit();
      btn_per.setVisibility(View.INVISIBLE);
      current_fragment--;
    }else  if(current_fragment == 3){
      btn_next.setText("بعدی");
      fm.beginTransaction().hide(intro3).show(intro2).commit();
      btn_per.setVisibility(View.VISIBLE);
      current_fragment--;
    }
  }

  private void next(){
    setButtonsAnim(true);
    if(current_fragment == 1){
      fm.beginTransaction().hide(intro1).show(intro2).commit();
      btn_per.setVisibility(View.VISIBLE);
      current_fragment++;
    }else if(current_fragment == 2){
      fm.beginTransaction().hide(intro2).show(intro3).commit();
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
}
