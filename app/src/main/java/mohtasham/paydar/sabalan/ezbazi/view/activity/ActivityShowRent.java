package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.squareup.picasso.Picasso;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;


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

  Game game;

  RentService service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_rent);

    setupViews();

    Bundle extras = getIntent().getExtras();
    prepareGame(extras ,savedInstanceState);

  }

  private void setupViews(){
    img_back = (ImageView) findViewById(R.id.img_back);
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

  private void prepareGame(Bundle extras, Bundle savedInstanceState){
    int id ;
    if(extras != null) {
     id = extras.getInt("ID");
    }else {
     id = (int) savedInstanceState.getSerializable("ID");
    }

    service = new RentService(ActivityShowRent.this);
    service.getSpecialRent(id, new RentService.onSpecialRentReceived() {
      @Override
      public void onReceived(int status, String message, Game game) {
        if(status != 1){
          MyViews.makeText(ActivityShowRent.this, message, Toast.LENGTH_SHORT);
        }else {
          ActivityShowRent.this.game = game;
          fillViews();
        }
      }
    });

  }


  private void fillViews(){
    txt_name.setText(game.getName());
    txt_console.setText(game.getConsole_name());

    String genres = "";
    for(int i=0 ; i<game.getGenres().size() ; i++){
      genres += game.getGenres().get(i);
    }
    txt_genres.setText(genres);
    txt_release_date.setText("تاریخ عرضه: " + game.getProduction_date());

    String cover_image = "";
    for (int i=0 ; i<game.getPhotos().size() ; i++){
//      if (game.getPhotos().get(i).getHeight() == R.dimen.photo_cover_height){
        cover_image = game.getPhotos().get(0).getUrl();
        break;
//      }
    }
    Picasso.with(ActivityShowRent.this).
      load(cover_image)
//      .placeholder()
      .into(img_game_cover);

    if(game.getVideos().size() > 0){
      img_game.setVisibility(View.GONE);
      playVideo();
    }else {
      img_game.setVisibility(View.VISIBLE);
      vdo_game.setVisibility(View.GONE);
      String main_image = "";
      for (int i=0 ; i<game.getPhotos().size() ; i++){
//        if (game.getPhotos().get(i).getHeight() == R.dimen.photo_main_height){
          cover_image = game.getPhotos().get(0).getUrl();
          break;
//        }
      }
    }
  }


  private void playVideo(){
//    getWindow().setFormat(PixelFormat.TRANSLUCENT);
//    MediaController mediaController = new MediaController(ActivityShowRent.this);
//    mediaController.setAnchorView(vdo_game);
//    vdo_game.setMediaController(mediaController);

    vdo_game.setVideoURI(Uri.parse(game.getVideos().get(0).getUrl()));
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
