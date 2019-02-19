package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListRentAdapter;
import sabalan.paydar.mohtasham.ezibazi.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListRent extends AppCompatActivity {

  RecyclerView rcv_list_rents;
  RentService apiService;
  ImageView img_back;
  TextView txt_page_name;

  AVLoadingIndicatorView avl_center, avl_bottom;
  Paginate paginate;
  ListRentAdapter listAdapter;
  int page_num = 1;

  List<RentType> rentTypes = G.rentTypes;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_game);

    setupViews();
    setTypeFace();

    rcv_list_rents.setLayoutManager(new GridLayoutManager(ActivityListRent.this,1, GridLayoutManager.VERTICAL,false));
    getRentTypes();
//    getRents();

    txt_page_name.setText("لیست بازی های اجاره ای");
    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityListRent.this.finish();
      }
    });

    rcv_list_rents.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1)) {
          if (paginate != null) {
            if (page_num != paginate.getLast_page()) {
              page_num++;
              avl_bottom.setVisibility(View.VISIBLE);
              getRents();
            }
          }
        }
      }
    });
  }

  private void setupViews(){
    rcv_list_rents = (RecyclerView) findViewById(R.id.rcv_list_game);
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);

    avl_center = findViewById(R.id.avl_center);
    avl_bottom = findViewById(R.id.avl_bottom);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityListRent.this));
  }



  private void getRentTypes(){
    apiService = new RentService(ActivityListRent.this);
    apiService.getRentTypes(new RentService.onRentTypesReceived() {
      @Override
      public void onReceived(int status, String message, List<RentType> rentTypes) {
        if (status == 1){
          ActivityListRent.this.rentTypes = rentTypes;
          getRents();
        }else {
          ActivityListRent.this.rentTypes = G.rentTypes;
//          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
        }
      }
    });
  }


  private void getRents(){
//    apiService = new RentService(G.context);
    apiService.getRents(page_num, new RentService.onRentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        avl_center.setVisibility(View.INVISIBLE);
        avl_bottom.setVisibility(View.INVISIBLE);
        if(status == 0){
//          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
        }else {
          ActivityListRent.this.paginate = paginate;
          if(page_num == 1) {
            listAdapter = new ListRentAdapter(ActivityListRent.this, games);
            SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
            rcv_list_rents.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
          }else {
            listAdapter.notifyData(games);
          }
        }
      }
    });
  }




  protected void onStart() {
    super.onStart();

//    connectivityListener = new ConnectivityListener(lyt_action);
    registerReceiver(G.connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(G.connectivityListener);
  }

}
