package sabalan.paydar.mohtasham.ezibazi.view.fragment.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.AccountService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.UserDetailService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment.FinanceService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ticket.TicketService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.auth.Auth;
import sabalan.paydar.mohtasham.ezibazi.controller.system.config.AppConfig;
import sabalan.paydar.mohtasham.ezibazi.controller.system.helper.HelperText;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.SettingPrefManager;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.UserPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
import sabalan.paydar.mohtasham.ezibazi.model.User;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityLogin;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityTicket;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;


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
  TextView txt_show_setting;
  TextView txt_show_video;
  SwitchCompat swch_show_video;


  View view;

  UserPrefManager prefManager;
  SettingPrefManager settingPrefManager;

  Dialog dialog;
  Button btn_go_to_bank;
  TextView txt_dialog_head;
  EditText edt_amount;
  AVLoadingIndicatorView avl_pay;
  int amount = 0;
  boolean is_to_change = true;

  boolean is_pause = false;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_profile, container, false);

    setupViews();
    setTypeFace();

    prefManager = new UserPrefManager(getContext());
    settingPrefManager = new SettingPrefManager(getContext());

    //get new tickets count (async)
    receiveNewTicketsCount();
    initializeSetting();



    Auth.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        if(!isLoggedIn){
          lyt_user_detail.setVisibility(View.GONE);
          btn_login.setVisibility(View.VISIBLE);
        }else {
          lyt_user_detail.setVisibility(View.VISIBLE);
          btn_login.setVisibility(View.GONE);
//          getUserDetail();
          txt_full_name.setText(new UserPrefManager(getContext()).getUser().getFull_name());
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



    btn_charge_account.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!G.isLoggedIn){
          Intent intent = new Intent(getActivity(), ActivityLogin.class);
          startActivity(intent);
        }else {
          dialog.show();
        }
      }
    });



    btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getContext(), ActivityLogin.class);
          getContext().startActivity(intent);
        }
      });



    edt_amount.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
//        String text = edt_amount.getText().toString();
//        text = HelperText.splitedPersianToLatin(text);
//        amount = Integer.valueOf(text);
//        String splitedText = HelperText.split_price(text);
//        edt_amount.setText(splitedText);
//        text = HelperText.splitedPersianToLatin(edt_amount.getText().toString());
//        int price = Integer.valueOf(text);
//        Toast.makeText(getContext(), "amount = " + amount, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "amount = " + price, Toast.LENGTH_SHORT).show();
      }
    });


    btn_go_to_bank.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!checkEntry()){
          return;
        }
       increaseCredit();
      }
    });

    swch_show_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
          settingPrefManager.savePlayVideo(1);
        }else {
          settingPrefManager.savePlayVideo(0);
        }
      }
    });






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
    txt_show_setting = view.findViewById(R.id.txt_show_setting);
    txt_show_video = view.findViewById(R.id.txt_show_video);
    swch_show_video = view.findViewById(R.id.swch_show_video);

    dialog = new Dialog(getContext());
    dialog.setContentView(R.layout.custom_dialog_increase_credit);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    btn_go_to_bank = dialog.findViewById(R.id.btn_go_to_bank);
    txt_dialog_head = dialog.findViewById(R.id.txt_dialog_head);
    edt_amount = dialog.findViewById(R.id.edt_amount);
    avl_pay = dialog.findViewById(R.id.avl_pay);
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
    txt_show_setting.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_show_video.setTypeface(MyViews.getIranSansLightFont(getContext()));

    btn_go_to_bank.setTypeface(MyViews.getIranSansUltraLightFont(getContext()));
    txt_dialog_head.setTypeface(MyViews.getIranSansUltraLightFont(getContext()));
    edt_amount.setTypeface(MyViews.getIranSansUltraLightFont(getContext()));

  }



  private void initializeSetting(){
    int is_play_video = settingPrefManager.getPlayVideos();
    if (is_play_video == 2){
      settingPrefManager.savePlayVideo(1);
      swch_show_video.setChecked(true);
    }else if(is_play_video == 0){
      swch_show_video.setChecked(false);
    }else if (is_play_video == 1){
      swch_show_video.setChecked(true);
    }
  }


  private void logoutUser(){
    prefManager = new UserPrefManager(getContext());
    User user = new User();
    user.setToken("");
    user.setFull_name("");
    prefManager.saveUser(user);
    G.isLoggedIn = false;
    G.initializeLogin();

    lyt_user_detail.setVisibility(View.GONE);
    btn_login.setVisibility(View.VISIBLE);
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


  private boolean checkEntry(){
    String text = edt_amount.getText().toString();
    if(text.length() < 1){
      MyViews.makeText((AppCompatActivity) getActivity(), "هیچ مبلغی وارد نشده است", Toast.LENGTH_SHORT);
      return false;
    } else if(Integer.parseInt(text) < 100){
      MyViews.makeText((AppCompatActivity) getActivity(), "مبلغ وارد شده حداقل باید 100 تومان باشد", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }

  private void receiveNewTicketsCount(){
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
      public void run() {
        getNewTicketsCount();
        if (G.isLoggedIn) {
          handler.postDelayed(this, AppConfig.NEW_TICKETS_CHECK_TIME_MS);
        }else {
          return;
        }
      }
    };

    handler.post(runnable);

  }


  private void receiveUserFinance(){
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
      public void run() {
        getUserFinance();
        if (G.isLoggedIn) {
          handler.postDelayed(this, AppConfig.TIME_GET_USER_FINANCE_REFRESH_MS);
        }else {
          return;
        }
      }
    };

    handler.post(runnable);

  }



  private void increaseCredit(){
    avl_pay.setVisibility(View.VISIBLE);
    FinanceService service = new FinanceService(getContext());
    JSONObject object = new JSONObject();
    amount = Integer.parseInt(edt_amount.getText().toString());
    try {
      object.put("amount", amount);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    service.getPayUrl(object, new FinanceService.onCreditComplete() {
      @Override
      public void onComplete(int status, String message, String url) {
        avl_pay.setVisibility(View.INVISIBLE);
        if (status == 1){
          edt_amount.setText("");
          dialog.dismiss();

          receiveUserFinance();

          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
          startActivity(browserIntent);
        }else {
          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        }
      }
    });

  }


  @Override
  public void onPause() {
    super.onPause();
    is_pause = true;
  }
}
