package mohtasham.paydar.sabalan.ezbazi.view.fragment.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.AccountService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.UserDetailService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.ticket.TicketService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.config.AppConfig;
import mohtasham.paydar.sabalan.ezbazi.controller.system.helper.HelperText;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.Finance;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityLogin;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityTicket;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;


public class FragmentProfile extends Fragment {


  Button btn_login;
  LinearLayout lyt_user_detail;
  LinearLayout lyt_logout;
  LinearLayout lyt_ticket;
  TextView txt_full_name;
  TextView txt_user_balance, txt_show_user_balance;
  Button btn_charge_account;
  TextView txt_ticket;
  TextView txt_logout;
  TextView txt_show_admin_accounts;
  TextView txt_rules;
  TextView txt_new_ticket_count;

  View view;

  UserSharedPrefManager prefManager;

  boolean is_pause = false;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_profile, container, false);

    setupViews();
    setTypeFace();


    //get new tickets count (async)
    receiveNewTicketsCount();



    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        if(!isLoggedIn){
          lyt_user_detail.setVisibility(View.GONE);
          btn_login.setVisibility(View.VISIBLE);
        }else {
          lyt_user_detail.setVisibility(View.VISIBLE);
          btn_login.setVisibility(View.GONE);
//          getUserDetail();
          txt_full_name.setText(new UserSharedPrefManager(getContext()).getUser().getFull_name());
          getUserFinance();
        }
      }
    });





    lyt_ticket.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //reset tickets notification
        txt_new_ticket_count.setText("0");
        txt_new_ticket_count.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(getContext(), ActivityTicket.class);
        startActivity(intent);
      }
    });

    lyt_logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AccountService service = new AccountService(getContext());
        service.logout(new AccountService.onLogoutComplete() {
          @Override
          public void onComplete(int status, String message) {
            if(status == 1){
              logoutUser();
            }else {
              MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
            }
          }
        });
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
//    prefManager.saveCityId(329);


    return view;
  }


  private void setupViews() {
    btn_login = view.findViewById(R.id.btn_login);
    lyt_user_detail = view.findViewById(R.id.lyt_user_detail);
    lyt_logout = view.findViewById(R.id.lyt_logout);
    lyt_ticket = view.findViewById(R.id.lyt_ticket);
    txt_full_name = view.findViewById(R.id.txt_full_name);
    txt_user_balance = view.findViewById(R.id.txt_user_balance);
    txt_show_user_balance = view.findViewById(R.id.txt_show_user_balance);
    btn_charge_account = view.findViewById(R.id.btn_charge_account);
    txt_ticket = view.findViewById(R.id.txt_ticket);
    txt_logout = view.findViewById(R.id.txt_logout);
    txt_show_admin_accounts = view.findViewById(R.id.txt_show_admin_accounts);
    txt_rules = view.findViewById(R.id.txt_rules);
    txt_new_ticket_count = view.findViewById(R.id.txt_new_ticket_count);
  }

  private void setTypeFace() {
    btn_login.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_full_name.setTypeface(MyViews.getIranSansMediumFont(getContext()));
    txt_user_balance.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_show_user_balance.setTypeface(MyViews.getIranSansLightFont(getContext()));
    btn_charge_account.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_ticket.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_logout.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_show_admin_accounts.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_rules.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_new_ticket_count.setTypeface(MyViews.getIranSansUltraLightFont(getContext()));
  }

  private void logoutUser(){
    prefManager = new UserSharedPrefManager(getContext());
    User user = new User();
    user.setToken("");
    user.setFull_name("");
    prefManager.saveUser(user);
    G.isLoggedIn = false;
    G.initializeLogin();

    lyt_user_detail.setVisibility(View.GONE);
    btn_login.setVisibility(View.VISIBLE);
  }


  private void getUserDetail(){

  }

  private void getUserFinance(){
    UserDetailService service = new UserDetailService(getContext());
    service.getUserFinance(new UserDetailService.onFinanceReceivedComplete() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onComplete(int status, String message, Finance finance) {
        if(status == 1){
          txt_user_balance.setText(HelperText.split_price(finance.getUser_balance()) + "  تومان  ");
        }
      }
    });
  }


  private void getNewTicketsCount(){
    TicketService service = new TicketService(getContext());
    service.getNewTicketsCount(new TicketService.onNewTicketsCount() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onReceived(int status, String message, int count) {
        if (status == 1) {
          if(count > 0) {
            txt_new_ticket_count.setText(Integer.toString(count));
            txt_new_ticket_count.setVisibility(View.VISIBLE);
          }
        }
      }
    });
  }


  private void receiveNewTicketsCount(){
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
      public void run() {
        getNewTicketsCount();
        if (!is_pause && G.isLoggedIn) {
          handler.postDelayed(this, AppConfig.NEW_TICKETS_TIME_MS);
        }else {
          return;
        }
      }
    };

    handler.post(runnable);


  }


  @Override
  public void onPause() {
    super.onPause();
    is_pause = true;
  }
}
