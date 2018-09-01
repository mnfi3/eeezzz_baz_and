package mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import mohtasham.paydar.sabalan.ezbazi.R;


public class FragmentShowGameToolbar extends Fragment {

  AppBarLayout app_bar;
  CollapsingToolbarLayout collapsing_toolbar;
  Toolbar toolbar;
  View view;
  ImageView img_game;
  RelativeLayout lyt_game_info_head;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_show_game_toolbar, container, false);

    setupViews();

    app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float offsetAlpha = (appBarLayout.getY() / app_bar.getTotalScrollRange());
        //Log.i(TAG, "onOffsetChanged: offsetAlpha = "+offsetAlpha);
        //collapsing_toolbar.setAlpha(offsetAlpha* -1);
        //toolbar.setAlpha(offsetAlpha* -1);
        lyt_game_info_head.setAlpha( 1 - (offsetAlpha * -1));
      }
    });

    return view;
  }

  private void setupViews(){
    app_bar= (AppBarLayout) view.findViewById(R.id.app_bar);
    collapsing_toolbar= (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
    toolbar= (Toolbar) view.findViewById(R.id.toolbar);
    img_game = view.findViewById(R.id.img_game);
    lyt_game_info_head = view.findViewById(R.id.lyt_game_info_head);
  }



}
