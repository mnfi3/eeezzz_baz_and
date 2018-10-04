package mohtasham.paydar.sabalan.ezbazi.view.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityLogin;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityTicket;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;


public class FragmentProfile extends Fragment {


  Button btn_login;
  LinearLayout lyt_user_detail;
  LinearLayout lyt_logout;
  LinearLayout lyt_ticket;

  View view;

  UserSharedPrefManager prefManager;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_profile, container, false);

    setupViews();
    setTypeFace();

    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        if(!isLoggedIn){
          lyt_user_detail.setVisibility(View.GONE);
          btn_login.setVisibility(View.VISIBLE);
        }else {
          lyt_user_detail.setVisibility(View.VISIBLE);
          btn_login.setVisibility(View.GONE);
          getUserDetail();
          getUserFinance();
        }
      }
    });





    lyt_ticket.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContext(), ActivityTicket.class);
        startActivity(intent);
      }
    });

    lyt_logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        logoutUser();
      }
    });



      btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getContext(), ActivityLogin.class);
          getContext().startActivity(intent);
        }
      });



    prefManager = new UserSharedPrefManager(getContext());
    prefManager.saveCityId(329);


    return view;
  }


  private void setupViews() {
    btn_login = view.findViewById(R.id.btn_login);
    lyt_user_detail = view.findViewById(R.id.lyt_user_detail);
    lyt_logout = view.findViewById(R.id.lyt_logout);
    lyt_ticket = view.findViewById(R.id.lyt_ticket);
  }

  private void setTypeFace() {
    btn_login.setTypeface(MyViews.getIranSansLightFont(getContext()));
  }

  private void logoutUser(){
    prefManager = new UserSharedPrefManager(getContext());
    User user = new User();
    user.setToken("");
    user.setUser_name("");
    prefManager.saveUser(user);
    G.isLoggedIn = false;
    G.initializeLogin();

    lyt_user_detail.setVisibility(View.GONE);
    btn_login.setVisibility(View.VISIBLE);
  }


  private void getUserDetail(){

  }

  private void getUserFinance(){

  }









}
