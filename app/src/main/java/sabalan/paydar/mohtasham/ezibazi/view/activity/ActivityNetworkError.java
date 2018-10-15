package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.hardware.ConnectivityListener;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ActivityNetworkError extends AppCompatActivity {


  TextView txt_network_error;

  ConnectivityListener connectivityListener;
  CoordinatorLayout lyt_root;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network_error);

    setupViews();
    setTypeFace();

    Animation rotation = AnimationUtils.loadAnimation(ActivityNetworkError.this, R.anim.rotate_network_error);
    rotation.setFillAfter(true);
    txt_network_error.startAnimation(rotation);






  }



  private void setupViews(){
    txt_network_error = findViewById(R.id.txt_network_error);
    lyt_root = findViewById(R.id.lyt_root);
  }

  private void setTypeFace(){
    txt_network_error.setTypeface(MyViews.getIranSansLightFont(ActivityNetworkError.this));

  }





  protected void onStart() {
    super.onStart();

//    connectivityListener = new ConnectivityListener(lyt_root);
    registerReceiver(G.connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(G.connectivityListener);
  }

}
