package mohtasham.paydar.sabalan.ezbazi.view.fragment.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;


public class FragmentProfile extends Fragment {


  EditText edt_search;

  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_profile, container, false);

    setupViews();
    setTypeFace();

    UserSharedPrefManager prefManager = new UserSharedPrefManager(getContext());
    prefManager.saveCityId(329);


    return view;
  }


  private void setupViews() {
  }

  private void setTypeFace() {
  }









}
