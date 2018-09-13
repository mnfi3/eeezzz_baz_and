package mohtasham.paydar.sabalan.ezbazi.view.fragment.activity;

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


public class FragmentActivity extends Fragment {

  private static final String TAG = "FragmentActivity";


  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_activity, container, false);
//    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
    setupViews();



    return view;
  }

  private void setupViews(){

  }
}
