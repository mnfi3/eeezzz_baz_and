package mohtasham.paydar.sabalan.ezbazi.view.fragment.home.sub;

import android.content.Intent;
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
import android.widget.ToggleButton;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.RentMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityListRent;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentRents extends Fragment {
  AppCompatActivity activity;


  LinearLayout lyt_rents;
  RecyclerView rcv_rents;
  RentService apiService;
  int page_num = 1;
  TextView txt_show_rent_list;
  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main_rents, container, false);
    setupViews();
    setTypeFace();
    lyt_rents.setVisibility(View.INVISIBLE);

    rcv_rents.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));
    getRents();

    txt_show_rent_list.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(G.context, ActivityListRent.class);
        startActivity(intent);
      }
    });





    return view;
  }

  private void setupViews(){
    rcv_rents = view.findViewById(R.id.rcv_rents);
    txt_show_rent_list = view.findViewById(R.id.txt_show_rent_list);
    lyt_rents = view.findViewById(R.id.lyt_rents);
  }


  private void getRents(){
    apiService = new RentService(G.context);
    apiService.getRents(1, new RentService.onRentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games, Paginate paginate) {
        if(status == 0){
          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        }else {
          RentMainAdapter listAdapter = new RentMainAdapter(getActivity(), games);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          rcv_rents.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
          setAnimation();
          lyt_rents.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right);
    lyt_rents.setAnimation(anim);
    anim.start();
  }


  private void setTypeFace(){
    txt_show_rent_list.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }





}
