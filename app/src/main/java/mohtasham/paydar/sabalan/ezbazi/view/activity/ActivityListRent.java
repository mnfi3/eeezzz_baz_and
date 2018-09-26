package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListRentAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListRent extends AppCompatActivity {

  RecyclerView rcv_list_rents;
  RentService apiService;
  ImageView img_back;
  TextView txt_page_name;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_game);

    setupViews();
    setTypeFace();

    rcv_list_rents.setLayoutManager(new GridLayoutManager(ActivityListRent.this,1, GridLayoutManager.VERTICAL,false));
    getRents();

    txt_page_name.setText("لیست بازی های اجاره ای");
    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityListRent.this.finish();
      }
    });
  }

  private void setupViews(){
    rcv_list_rents = (RecyclerView) findViewById(R.id.rcv_list_game);
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityListRent.this));
  }



  private void getRents(){
    apiService = new RentService(G.context);
    apiService.getRents(1, new RentService.onRentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        if(status == 0){
          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
        }else {
          ListRentAdapter listAdapter = new ListRentAdapter(ActivityListRent.this, games);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
          rcv_list_rents.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
      }
    });
  }


}
