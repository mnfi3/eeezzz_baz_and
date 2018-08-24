package mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.PostMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ShopMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.ShopService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.GameForShop;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentShops extends Fragment {

  RecyclerView rcv_shops;
  ShopService apiService;
  int page_num = 1;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_shops, container, false);

    rcv_shops = view.findViewById(R.id.rcv_shops);
    rcv_shops.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));
    getShops();
    return view;
  }


  private void getShops(){
    apiService = new ShopService(G.context);
    apiService.getMainShops(1, new ShopService.onShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<GameForShop> games, Paginate paginate) {
        if(status == 0){
          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
        }else {
          ShopMainAdapter listAdapter=new ShopMainAdapter(G.context, games);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          rcv_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
        }
      }
    });
  }






}
