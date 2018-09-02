package mohtasham.paydar.sabalan.ezbazi.view.fragment.show_detail;

import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.VideoView;


import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import mohtasham.paydar.sabalan.ezbazi.R;


public class FragmentShowGameMedia extends Fragment {

  AppBarLayout app_bar;
  CollapsingToolbarLayout collapsing_toolbar;
  Toolbar toolbar;
  View view;
  ImageView img_game;
  RelativeLayout lyt_game_info_head;
  BetterVideoPlayer btvdo_game;
  VideoView vdo_game;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_show_game_media, container, false);

    setupViews();

//    app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//      @Override
//      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        float offsetAlpha = (appBarLayout.getY() / app_bar.getTotalScrollRange());
//        //Log.i(TAG, "onOffsetChanged: offsetAlpha = "+offsetAlpha);
//        //collapsing_toolbar.setAlpha(offsetAlpha* -1);
//        //toolbar.setAlpha(offsetAlpha* -1);
//        lyt_game_info_head.setAlpha( 1 - (offsetAlpha * -1));
//      }
//    });

    img_game.setVisibility(View.INVISIBLE);
//    btvdo_game.setVisibility(View.VISIBLE);
//    btvdo_game.setSource(Uri.parse("http://192.168.10.83/izi-bazi.ud/uploads/videos/1.mp4"));
    vdo_game.setVisibility(View.VISIBLE);
    vdo_game.setVideoURI(Uri.parse("http://192.168.10.83/izi-bazi.ud/uploads/videos/1.mp4"));
    vdo_game.start();
    vdo_game.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        vdo_game.start();
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
//    btvdo_game = view.findViewById(R.id.btvdo_game);
    vdo_game = view.findViewById(R.id.vdo_game);
  }



}
