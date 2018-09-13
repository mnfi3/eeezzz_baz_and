package mohtasham.paydar.sabalan.ezbazi.view.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.sub.FragmentPosts;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.sub.FragmentRents;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.sub.FragmentShops;


public class FragmentHome extends Fragment {

  private static final String TAG = "FragmentHome";

//  DrawerLayout drawerLayout;

  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_home, container, false);
//    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
    setupViews();

    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
    ft.replace(R.id.lyt_main_posts, new FragmentPosts());
    ft.replace(R.id.lyt_main_shops, new FragmentRents());
    ft.replace(R.id.lyt_main_rents, new FragmentShops());
    ft.commit();

//    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//    ft2.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
//    ft2.commit();


    return view;
  }

  private void setupViews(){

  }
}
