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
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.account.AccountService;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ActivityRegister extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  EditText edt_full_name, edt_email, edt_password, edt_re_password;
  TextView txt_show_pass;
  Button btn_register;
  AccountService service;
  CheckBox chk_show_pass;
  AVLoadingIndicatorView avl_register;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    setupViews();
    setTypeFace();
    txt_page_name.setText("یجاد حساب کاربری");

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
        avl_register.setVisibility(View.VISIBLE);
        String full_name = edt_full_name.getText().toString();
        String email = edt_email.getText().toString();
        final String password = edt_password.getText().toString();

        JSONObject object = new JSONObject();
        try {
          object.put("full_name", full_name);
          object.put("email", email);
          object.put("password", password);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        service = new AccountService(ActivityRegister.this);
        service.register(object, new AccountService.onRegisterComplete() {
          @Override
          public void onComplete(int status, String message, String token, User user) {
            avl_register.setVisibility(View.INVISIBLE);
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
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
    edt_full_name = findViewById(R.id.edt_full_name);
    edt_email = findViewById(R.id.edt_email);
    edt_password = findViewById(R.id.edt_password);
    edt_re_password = findViewById(R.id.edt_re_password);
    txt_show_pass = findViewById(R.id.txt_show_pass);
    btn_register = findViewById(R.id.btn_register);
    chk_show_pass = findViewById(R.id.chk_show_pass);
    avl_register = findViewById(R.id.avl_register);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    edt_full_name.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    edt_email.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    edt_password.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    edt_re_password.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    txt_show_pass.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
    btn_register.setTypeface(MyViews.getIranSansLightFont(ActivityRegister.this));
  }


  private boolean checkEntry(){
    if(
      edt_full_name.getText().toString().length()<1 ||
      edt_email.getText().toString().length()<1 ||
      edt_password.getText().toString().length()<1 ||
      edt_re_password.getText().toString().length()<1
      ){
      MyViews.makeText(ActivityRegister.this,"لطفا اطلاعات خود را کامل وارد کنید", Toast.LENGTH_SHORT);
      return false;
    }
    else if( edt_full_name.getText().toString().length() < 6){
      MyViews.makeText(ActivityRegister.this,"لطفا نام و نام خانوادگی خود را با دقت وارد کنید",Toast.LENGTH_SHORT);
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





}
