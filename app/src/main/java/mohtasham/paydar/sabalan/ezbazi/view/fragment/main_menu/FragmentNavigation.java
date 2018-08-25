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

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityLogin;


public class FragmentNavigation extends Fragment {

  View view;
  LinearLayout lyt_login;
  DrawerLayout drawerLayout;


  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main_navigation, container, false);
    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
    drawerLayout = (DrawerLayout) view1;

    setupViews();



    lyt_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(G.context, ActivityLogin.class);
        startActivity(intent);
        drawerLayout.closeDrawers();
      }
    });




    return view;
  }

  private void setupViews(){
    lyt_login = view.findViewById(R.id.lyt_login);
  }






}
