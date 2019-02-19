package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail;

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
import sabalan.paydar.mohtasham.ezibazi.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.RelatedShopAdapter;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentRelatedShops extends Fragment {

  int game_id;
  public void setId(int game_id){
    this.game_id = game_id;
  }


  private static final String TAG = "FragmentRelatedShops";

  LinearLayout lyt_related_shops;
  RecyclerView rcv_related_shops;
  ShopService apiService;
  int page_num = 1;
  TextView txt_related_shops;


  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_related_shops, container, false);
    setupViews();
    setTypeFace();
    lyt_related_shops.setVisibility(View.GONE);

    rcv_related_shops.setLayoutManager((new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true)));

    game_id = getArguments().getInt("game_id");
    getRelatedRents();




    return view;
  }

  private void setupViews(){
    rcv_related_shops = view.findViewById(R.id.rcv_related_shops);
    txt_related_shops = view.findViewById(R.id.txt_related_shops);
    lyt_related_shops = view.findViewById(R.id.lyt_related_shops);
  }


  private void setTypeFace(){
    txt_related_shops.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }



  private void getRelatedRents(){
    apiService = new ShopService(getContext());
    apiService.getRelatedShops(game_id, new ShopService.onRelatedShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games) {
        if (status == 0) {
//          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        } else {
          if(games.size() > 0) {
            RelatedShopAdapter listAdapter = new RelatedShopAdapter(getActivity(), games);
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
            rcv_related_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
            setAnimation();
            lyt_related_shops.setVisibility(View.VISIBLE);
          }
        }
      }
    });
  }

  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_enter_from_right);
    lyt_related_shops.setAnimation(anim);
    anim.start();
  }
}
