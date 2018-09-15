package mohtasham.paydar.sabalan.ezbazi.view.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.PostMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityListPost;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentSearch extends Fragment {


  EditText edt_search;

  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_search, container, false);

    setupViews();
    setTypeFace();

    return view;
  }


  private void setupViews() {
    edt_search = view.findViewById(R.id.edt_search);
  }

  private void setTypeFace() {
    edt_search.setTypeface(MyViews.getIranSansLightFont(getContext()));
  }









}
