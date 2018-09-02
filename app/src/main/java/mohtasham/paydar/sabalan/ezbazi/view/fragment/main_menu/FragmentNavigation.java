package mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.User;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityLogin;


public class FragmentNavigation extends Fragment {

  View view;
  LinearLayout lyt_login;
  DrawerLayout drawerLayout;
  TextView txt_users_name;
  TextView txt_register_login;

  private boolean isLoggedIn = false;


  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main_navigation, container, false);
    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
    drawerLayout = (DrawerLayout) view1;

    setupViews();


    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        FragmentNavigation.this.isLoggedIn = isLoggedIn;
        if (isLoggedIn){
          txt_users_name.setText(user.getUser_name());
          txt_register_login.setText("داشبورد");
        }
      }
    });


      lyt_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(!isLoggedIn) {
            Intent intent = new Intent(G.context, ActivityLogin.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
          }
        }
      });




    return view;
  }

  private void setupViews(){
    lyt_login = view.findViewById(R.id.lyt_login);
    txt_users_name = view.findViewById(R.id.txt_users_name);
    txt_register_login = view.findViewById(R.id.txt_register_login);
  }


  @Override
  public void onResume() {
    super.onResume();
    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        FragmentNavigation.this.isLoggedIn = isLoggedIn;
        if (isLoggedIn){
          txt_users_name.setText(user.getUser_name());
          txt_register_login.setText("داشبورد");
        }
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        FragmentNavigation.this.isLoggedIn = isLoggedIn;
        if (isLoggedIn){
          txt_users_name.setText(user.getUser_name());
          txt_register_login.setText("داشبورد");
        }
      }
    });
  }

  @Override
  public void onStop() {
    super.onStop();
    G.loginCheck(new G.onLoginCheck() {
      @Override
      public void onCheck(User user, boolean isLoggedIn) {
        FragmentNavigation.this.isLoggedIn = isLoggedIn;
        if (isLoggedIn){
          txt_users_name.setText(user.getUser_name());
          txt_register_login.setText("داشبورد");
        }
      }
    });
  }
}
