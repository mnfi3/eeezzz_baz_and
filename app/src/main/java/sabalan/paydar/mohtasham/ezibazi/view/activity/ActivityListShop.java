package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.adapter.recyclerview.ListShopAdapter;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListShop extends AppCompatActivity {

  RecyclerView rcv_list_shops;
  ShopService apiService;

  ImageView img_back;
  TextView txt_page_name;

  AVLoadingIndicatorView avl_center, avl_bottom;
  int page_num = 1;
  Paginate paginate;
  ListShopAdapter listAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_game);

    setupViews();
    setTypeFace();

    rcv_list_shops.setLayoutManager(new GridLayoutManager(G.context,1,GridLayoutManager.VERTICAL,false));
    getShops();

    txt_page_name.setText("لیست بازی های فروشی");
    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityListShop.this.finish();
      }
    });



    rcv_list_shops.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1)) {
          if (paginate != null) {
            if (page_num != paginate.getLast_page()) {
              page_num++;
              avl_bottom.setVisibility(View.VISIBLE);
              getShops();
            }
          }
        }
      }
    });

  }

  private void setupViews(){
    rcv_list_shops = (RecyclerView) findViewById(R.id.rcv_list_game);
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);

    avl_center = findViewById(R.id.avl_center);
    avl_bottom = findViewById(R.id.avl_bottom);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityListShop.this));
  }


  private void getShops(){
    apiService = new ShopService(G.context);
    apiService.getMainShops(page_num, new ShopService.onShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        avl_center.setVisibility(View.INVISIBLE);
        avl_bottom.setVisibility(View.INVISIBLE);
        if(status == 0){
//          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
        }else {
          ActivityListShop.this.paginate = paginate;
          if(page_num == 1) {
            listAdapter = new ListShopAdapter(ActivityListShop.this, games);
//          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
            SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
//          rcv_list_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
            rcv_list_shops.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
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
