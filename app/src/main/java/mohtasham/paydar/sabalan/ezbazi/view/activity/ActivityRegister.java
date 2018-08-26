package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.AccountService;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ActivityRegister extends AppCompatActivity {

  ImageView img_back;
  EditText edt_first_name, edt_last_name, edt_user_name, edt_mobile_number, edt_email, edt_password, edt_re_password;
  Button btn_register;
  AccountService service;
  CheckBox chk_show_pass;
  ProgressBar prg_register;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    setupViews();

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityRegister.this.finish();
      }
    });


    chk_show_pass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(chk_show_pass.isChecked()){
          edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
          edt_re_password.setInputType(InputType.TYPE_CLASS_TEXT);
        }else if(!chk_show_pass.isChecked()) {
          edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
          edt_re_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
      }
    });



    btn_register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!checkEntry()) return;
        prg_register.setVisibility(View.VISIBLE);
        String first_name = edt_first_name.getText().toString();
        String last_name = edt_last_name.getText().toString();
        String mobile_number = edt_mobile_number.getText().toString();
        String user_name = edt_user_name.getText().toString();
        String email = edt_email.getText().toString();
        final String password = edt_password.getText().toString();

        JSONObject object = new JSONObject();
        try {
          object.put("first_name", first_name);
          object.put("last_name", last_name);
          object.put("mobile_number", mobile_number);
          object.put("user_name", user_name);
          object.put("email", email);
          object.put("password", password);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        service = new AccountService(ActivityRegister.this);
        service.register(object, new AccountService.onRegisterComplete() {
          @Override
          public void onComplete(int status, String message, String token, User user) {
            prg_register.setVisibility(View.INVISIBLE);
            MyViews.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT);
            if(status == 1){
              MyViews.makeText(ActivityRegister.this, "لطفا وارد حساب کاربری خود شوید", Toast.LENGTH_SHORT);
              Intent intent = new Intent(ActivityRegister.this,ActivityLogin.class);
              intent.putExtra("EMAIL",edt_email.getText().toString());
              intent.putExtra("PASSWORD",edt_password.getText().toString());
              startActivity(intent);
              ActivityRegister.this.finish();
            }
          }
        });

      }
    });


  }

  private void setupViews(){
    img_back = (ImageView) findViewById(R.id.img_back);
    edt_first_name = (EditText) findViewById(R.id.edt_first_name);
    edt_last_name = (EditText) findViewById(R.id.edt_last_name);
    edt_user_name = (EditText) findViewById(R.id.edt_user_name);
    edt_mobile_number = (EditText) findViewById(R.id.edt_mobile_number);
    edt_email = (EditText) findViewById(R.id.edt_email);
    edt_password = (EditText) findViewById(R.id.edt_password);
    edt_re_password = (EditText) findViewById(R.id.edt_re_password);
    btn_register = (Button) findViewById(R.id.btn_register);
    chk_show_pass = (CheckBox) findViewById(R.id.chk_show_pass);
    prg_register = (ProgressBar) findViewById(R.id.prg_register);
  }


  private boolean checkEntry(){
    if(
      edt_first_name.getText().toString().length()<2 ||
      edt_last_name.getText().toString().length()<2 ||
      edt_email.getText().toString().length()<1 ||
      edt_mobile_number.getText().toString().length()<1 ||
      edt_user_name.getText().toString().length()<1 ||
      edt_password.getText().toString().length()<1 ||
      edt_re_password.getText().toString().length()<1
      ){
      MyViews.makeText(ActivityRegister.this,"لطفا اطلاعات خود را کامل وارد کنید", Toast.LENGTH_SHORT);
      return false;
    }



//    else if( edt_mobile_number.getText().toString().length() != 10){
//      MyViews.makeText(ActivityRegister.this,"شماره موبایل وارد شده اشتباه می باشد",Toast.LENGTH_SHORT);
//      return false;
//    }

    else if(!validUserName(edt_user_name.getText().toString())){
      MyViews.makeText(ActivityRegister.this,"نام کاربری باید متشکل از حروف انگلیسی و اعداد بوده و حداقل 5 کاراکتر داشته باشد",Toast.LENGTH_SHORT);
      return false;
    }


    else if(!validMobile(edt_mobile_number.getText().toString())){
      MyViews.makeText(ActivityRegister.this,"شماره موبایل وارد شده صحیح نمی باشد",Toast.LENGTH_SHORT);
      return false;
    }
    else if( edt_password.getText().toString().length() < 6){
      MyViews.makeText(ActivityRegister.this,"کلمه عبور باید حداقل 6 کاراکتر داشته باشد",Toast.LENGTH_SHORT);
      return false;
    }
    else if(!validEmail(edt_email.getText().toString())){
      MyViews.makeText(ActivityRegister.this,"ایمیل وارد شده صحیح نمی باشد",Toast.LENGTH_SHORT);
      return false;
    }
    else if(!(edt_password.getText().toString().equals(edt_re_password.getText().toString()))){
      MyViews.makeText(ActivityRegister.this,"کلمه های عبور وارد شده مطابقت ندارند",Toast.LENGTH_SHORT);
      return false;
    }


    return true;
  }


  private boolean validEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return pattern.matcher(email).matches();
  }

  private boolean validMobile(String mobile) {
    if (mobile.length() != 11 || mobile.charAt(0) != '0') return false;

    for (int i = 0 ; i<mobile.length() ; i++){
      if (mobile.charAt(i) > '9' || mobile.charAt(i) < '0'){
        return false;
      }
    }
    return true;
  }


  private boolean validUserName(String user_name){
    if (user_name.length() < 5) return false;

//    for (int i=0 ; i<user_name.length() ; i++){
//      if (
//          user_name.charAt(i) < '0' &&
//          user_name.charAt(i) > '9' &&
//          user_name.charAt(i) < 'a' &&
//          user_name.charAt(i) > 'z' &&
//          user_name.charAt(i) < 'A' &&
//          user_name.charAt(i) > 'Z'
//        ){
//        return false;
//      }
//    }

    return true;
  }



}
