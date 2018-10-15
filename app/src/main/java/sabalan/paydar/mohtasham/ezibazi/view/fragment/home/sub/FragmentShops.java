package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.adapter.recyclerview.ShopMainAdapter;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityListShop;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentShops extends Fragment {

  LinearLayout lyt_shops;
  RecyclerView rcv_shops;
  ShopService apiService;
  int page_num = 1;
  TextView txt_show_shops_list;
  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main_shops, container, false);

    setupViews();
    setTypeFace();
    lyt_shops.setVisibility(View.INVISIBLE);

    rcv_shops.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));
    getShops();

    txt_show_shops_list.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(G.context, ActivityListShop.class);
        startActivity(intent);
      }
    });

    return view;
  }


  private void setupViews(){
    rcv_shops = view.findViewById(R.id.rcv_shops);
    txt_show_shops_list = view.findViewById(R.id.txt_show_shops_list);
    lyt_shops = view.findViewById(R.id.lyt_shops);
  }

  private void getShops(){
    apiService = new ShopService(G.context);
    apiService.getMainShops(1, new ShopService.onShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        if(status == 0){
//          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        }else {
          ShopMainAdapter listAdapter=new ShopMainAdapter(G.context, games);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          rcv_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
          setAnimation();
          lyt_shops.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_left);
    lyt_shops.setAnimation(anim);
    anim.start();
  }


  private void setTypeFace(){
    txt_show_shops_list.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }




}
