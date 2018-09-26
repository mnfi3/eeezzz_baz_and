package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListShopAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.ShopService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListShop extends AppCompatActivity {

  RecyclerView rcv_list_shops;
  ShopService apiService;

  ImageView img_back;
  TextView txt_page_name;

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

  }

  private void setupViews(){
    rcv_list_shops = (RecyclerView) findViewById(R.id.rcv_list_game);
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityListShop.this));
  }


  private void getShops(){
    apiService = new ShopService(G.context);
    apiService.getMainShops(1, new ShopService.onShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        if(status == 0){
          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
        }else {
          ListShopAdapter listAdapter=new ListShopAdapter(ActivityListShop.this, games);
//          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
//          rcv_list_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
          rcv_list_shops.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        }
      }
    });
  }
}
