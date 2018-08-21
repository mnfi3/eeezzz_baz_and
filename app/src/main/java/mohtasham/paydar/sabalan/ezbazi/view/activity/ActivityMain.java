package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import mohtasham.paydar.sabalan.ezbazi.R;

public class ActivityMain extends AppCompatActivity {

  ImageView img_open_navigation;
  DrawerLayout drawerLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    setupViews();


    img_open_navigation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawerLayout.openDrawer(Gravity.RIGHT);
      }
    });
  }

  private void setupViews(){
    img_open_navigation = (ImageView) findViewById(R.id.img_open_navigation);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
  }
}
