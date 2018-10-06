package mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.RelatedRentAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityShowRent;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentRelatedRents extends Fragment {

  private int game_id;
 public void setId(int game_id){
   this.game_id = game_id;
 }



  private static final String TAG = "FragmentRelatedRents";

  LinearLayout lyt_related_rents;
  RecyclerView rcv_related_rents;
  RentService apiService;
  int page_num = 1;
  TextView txt_related_rents;

  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_related_rents, container, false);
    setupViews();
    setTypeFace();
    lyt_related_rents.setVisibility(View.GONE);

    rcv_related_rents.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));

    game_id = getArguments().getInt("game_id");
    getRelatedRents();




    return view;
  }

  private void setupViews(){
    rcv_related_rents = view.findViewById(R.id.rcv_related_rents);
    txt_related_rents = view.findViewById(R.id.txt_related_rents);
    lyt_related_rents = view.findViewById(R.id.lyt_related_rents);
  }

  private void setTypeFace(){
    txt_related_rents.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }



  private void getRelatedRents(){
    apiService = new RentService(getContext());
    apiService.getRelatedRents(game_id, new RentService.onRelatedRentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games) {
        if (status == 0) {
          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        } else {
          if(games.size() > 0) {
            RelatedRentAdapter listAdapter = new RelatedRentAdapter(getActivity(), games);
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
            rcv_related_rents.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
            setAnimation();
            lyt_related_rents.setVisibility(View.VISIBLE);
          }
        }
      }
    });
  }

    private void setAnimation(){
      Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right);
      lyt_related_rents.setAnimation(anim);
      anim.start();
    }

}
