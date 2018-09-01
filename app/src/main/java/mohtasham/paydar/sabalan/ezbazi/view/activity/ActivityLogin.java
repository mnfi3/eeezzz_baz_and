package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.AccountService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ActivityLogin extends AppCompatActivity {


  TextView txt_register;
  ImageView img_back;
  EditText edt_email, edt_password;
  Button btn_login;
  AccountService service;
  CheckBox chk_show_pass;
  ProgressBar prg_login;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    setupViews();


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
        prg_login.setVisibility(View.VISIBLE);

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
            prg_login.setVisibility(View.INVISIBLE);
            MyViews.makeText(ActivityLogin.this, message, Toast.LENGTH_SHORT);
            if(status == 1){
              UserSharedPrefManager prefManager = new UserSharedPrefManager(ActivityLogin.this);
              prefManager.saveUser(user);
              ActivityLogin.this.finish();
            }
          }
        });

      }
    });

  }

  private void setupViews(){
    txt_register = (TextView) findViewById(R.id.txt_register);
    img_back = (ImageView) findViewById(R.id.img_back);
    edt_email = (EditText) findViewById(R.id.edt_email);
    edt_password = (EditText) findViewById(R.id.edt_password);
    btn_login = (Button) findViewById(R.id.btn_login);
    chk_show_pass = (CheckBox) findViewById(R.id.chk_show_pass);
    prg_login = (ProgressBar) findViewById(R.id.prg_login);
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
}
