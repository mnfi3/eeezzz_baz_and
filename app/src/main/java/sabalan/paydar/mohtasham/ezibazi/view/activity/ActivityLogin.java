package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.AccountService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.firebase.FireBaseApiService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.hardware.ConnectivityListener;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.FcmPrefManager;
import sabalan.paydar.mohtasham.ezibazi.controller.system.pref_manager.UserPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.Device;
import sabalan.paydar.mohtasham.ezibazi.model.Fcm;
import sabalan.paydar.mohtasham.ezibazi.model.User;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ActivityLogin extends AppCompatActivity {


  ConnectivityListener connectivityListener;
  LinearLayout lyt_root;

  TextView txt_register;
  TextView txt_page_name;
  TextView txt_show_pass;
  TextView txt_forget_password;
  ImageView img_back;
  EditText edt_email, edt_password;
  Button btn_login;
  AccountService service;
  CheckBox chk_show_pass;
  AVLoadingIndicatorView avl_login;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    setupViews();
    setTypeFace();

    txt_page_name.setText("ورود به حساب کاربری");


    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if(extras != null) {
        String email = extras.getString("EMAIL");
        String password = extras.getString("PASSWORD");
        edt_email.setText(email);
        edt_password.setText(password);
      }
    } else {
      String email = (String) savedInstanceState.getSerializable("EMAIL");
      String password = (String) savedInstanceState.getSerializable("PASSWORD");
      edt_email.setText(email);
      edt_password.setText(password);
    }





    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityLogin.this.finish();
      }
    });


    chk_show_pass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(chk_show_pass.isChecked()){
          edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
        }else if(!chk_show_pass.isChecked()) {
          edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
      }
    });


    txt_register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
          startActivity(intent);
        }
      });


    btn_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!checkEntry()) return;
        avl_login.setVisibility(View.VISIBLE);

        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        JSONObject object = new JSONObject();
        try {
          object.put("email", email);
          object.put("password", password);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        service = new AccountService(ActivityLogin.this);
        service.login(object, new AccountService.onLoginComplete() {
          @Override
          public void onComplete(int status, String message, String token, User user) {
            avl_login.setVisibility(View.INVISIBLE);
            MyViews.makeText(ActivityLogin.this, message, Toast.LENGTH_SHORT);
            if(status == 1){
              UserPrefManager prefManager = new UserPrefManager(ActivityLogin.this);
              prefManager.saveUser(user);
              G.isLoggedIn = true;
              G.initializeLogin();
              //update user fcm token in server
              sendFcmInfoToServer();

              //restart ActivityMenu
              ActivityMenu.getInstance().finish();
              Intent intent = new Intent(ActivityLogin.this, ActivityMenu.class);
              startActivity(intent);
              ActivityLogin.this.finish();
            }
          }
        });

      }
    });

  }


//  private void restartFragments(){
//    Fragment frg, frg2 = null;
//    frg = getSupportFragmentManager().findFragmentByTag("2");
//    frg2 = getSupportFragmentManager().findFragmentByTag("4");
//    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//    ft.detach(frg);
//    ft.detach(frg2);
//    ft.attach(frg);
//    ft.attach(frg2);
//    ft.commit();
//  }

  private void setupViews(){
    lyt_root = findViewById(R.id.lyt_root);
    txt_register = findViewById(R.id.txt_register);
    img_back = findViewById(R.id.img_back);
    edt_email = findViewById(R.id.edt_email);
    edt_password = findViewById(R.id.edt_password);
    btn_login = findViewById(R.id.btn_login);
    chk_show_pass = findViewById(R.id.chk_show_pass);
    avl_login =  findViewById(R.id.avl_login);
    txt_page_name = findViewById(R.id.txt_page_name);
    txt_show_pass = findViewById(R.id.txt_show_pass);
    txt_forget_password = findViewById(R.id.txt_forget_password);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityLogin.this));
    edt_email.setTypeface(MyViews.getIranSansLightFont(ActivityLogin.this));
    edt_password.setTypeface(MyViews.getIranSansLightFont(ActivityLogin.this));
    btn_login.setTypeface(MyViews.getIranSansLightFont(ActivityLogin.this));
    txt_show_pass.setTypeface(MyViews.getIranSansLightFont(ActivityLogin.this));
    txt_forget_password.setTypeface(MyViews.getIranSansMediumFont(ActivityLogin.this));
    txt_register.setTypeface(MyViews.getIranSansMediumFont(ActivityLogin.this));
  }


  private void sendFcmInfoToServer(){
    Fcm fcm = new FcmPrefManager(ActivityLogin.this).getFcm();
    JSONObject object = new JSONObject();
    try {
      object.put("device_type", "ANDROID");
      object.put("client_key", fcm.getClient_key());
      object.put("fcm_token", fcm.getToken());
    } catch (JSONException e) {
    }

    FireBaseApiService service = new FireBaseApiService(ActivityLogin.this);
    service.refreshFcmToken(object, new FireBaseApiService.onRefreshTokenReceived() {
      @Override
      public void onReceived(int status, String message, Device device) {
      }
    });
  }



  private boolean checkEntry(){
    if(
        edt_email.getText().toString().length()<1 ||
        edt_password.getText().toString().length()<1
      ){
      MyViews.makeText(ActivityLogin.this,"لطفا اطلاعات خود را کامل وارد کنید", Toast.LENGTH_SHORT);
      return false;
    }

    else if( edt_password.getText().toString().length() < 6){
      MyViews.makeText(ActivityLogin.this,"کلمه عبور خود را بادقت وارد کنید",Toast.LENGTH_SHORT);
      return false;
    }
    else if(!validEmail(edt_email.getText().toString())){
      MyViews.makeText(ActivityLogin.this,"ایمیل وارد شده صحیح نمی باشد",Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }


  private boolean validEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return pattern.matcher(email).matches();
  }



  protected void onStart() {
    super.onStart();

//    connectivityListener = new ConnectivityListener(lyt_root);
    registerReceiver(G.connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(G.connectivityListener);
  }
}
