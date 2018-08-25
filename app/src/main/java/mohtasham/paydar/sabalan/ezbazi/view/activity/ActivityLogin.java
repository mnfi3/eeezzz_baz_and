package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mohtasham.paydar.sabalan.ezbazi.R;

public class ActivityLogin extends AppCompatActivity {


  TextView txt_register;
  ImageView img_back;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    setupViews();

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityLogin.this.finish();
      }
    });
    

      txt_register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
          startActivity(intent);
        }
      });
  }

  private void setupViews(){
    txt_register = (TextView) findViewById(R.id.txt_register);
    img_back = (ImageView) findViewById(R.id.img_back);
  }
}
