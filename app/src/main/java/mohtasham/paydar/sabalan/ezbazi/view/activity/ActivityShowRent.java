package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import mohtasham.paydar.sabalan.ezbazi.R;


public class ActivityShowRent extends AppCompatActivity {
  private static final String TAG = "ActivityShowRent";

  ImageView img_back;
  TextView txt_page_name;
  AppBarLayout app_bar;
  CollapsingToolbarLayout collapsing_toolbar;
  Toolbar toolbar;

  VideoView vdo_game;
  ImageView img_game;
  ImageView img_game_cover;
  TextView txt_name, txt_console, txt_genres, txt_release_date;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_rent);

    setupViews();

    playVideo();



  }

  private void setupViews(){
    img_back = (ImageView) findViewById(R.id.img_game);
    txt_page_name = (TextView) findViewById(R.id.txt_page_name);

    app_bar = (AppBarLayout) findViewById(R.id.app_bar);
    collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    toolbar = (Toolbar) findViewById(R.id.toolbar);

    vdo_game = (VideoView) findViewById(R.id.vdo_game);
    img_game = (ImageView) findViewById(R.id.img_game);
    img_game_cover = (ImageView) findViewById(R.id.img_game_cover);
    txt_name = (TextView) findViewById(R.id.txt_name);
    txt_console = (TextView) findViewById(R.id.txt_console);
    txt_genres = (TextView) findViewById(R.id.txt_genres);
    txt_release_date = (TextView) findViewById(R.id.txt_release_date);
  }


  private void playVideo(){
    getWindow().setFormat(PixelFormat.TRANSLUCENT);
    MediaController mediaController = new MediaController(ActivityShowRent.this);
    mediaController.setAnchorView(vdo_game);

//    vdo_game.setMediaController(mediaController);
    vdo_game.setVideoURI(Uri.parse("http://192.168.10.83/izi-bazi.ud/uploads/videos/1.mp4"));
    vdo_game.requestFocus();
    vdo_game.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        vdo_game.start();
      }
    });
    vdo_game.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        vdo_game.start();
      }
    });
  }
}
