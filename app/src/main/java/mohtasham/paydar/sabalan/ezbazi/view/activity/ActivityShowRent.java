package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.makeramen.roundedimageview.RoundedImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.HelperText;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.RentType;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail.FragmentRelatedPost;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail.FragmentRelatedRents;


public class ActivityShowRent extends AppCompatActivity {
  private static final String TAG = "ActivityShowRent";



  ImageView img_back;
  TextView txt_page_name;
  AppBarLayout app_bar;
  CollapsingToolbarLayout collapsing_toolbar;
  Toolbar toolbar;

  VideoView vdo_game;
  ImageView img_game;
  RoundedImageView img_game_cover;
  TextView txt_name, txt_console, txt_genres, txt_show_genres, txt_release_date, txt_rate, txt_detail_age_class;
  TextView txt_region, txt_show_region;
  RoundedImageView img_detail_console_icon;
  TextView txt_detail_release_date;
  ImageView img_rate_star;
  ExpandableTextView expand_text_view;

  Game game;

  RentService service;

  NestedScrollView nested_scroll;
  TextView txt_rent_period;
  Button btn_rent;
  Button btn_rent_day_count_7, btn_rent_day_count_10, btn_rent_day_count_20, btn_rent_day_count_30;
  Button btn_comments;

  FragmentTransaction ft;


  //for rent type initialize
  List<RentType> rentTypes = G.rentTypes;
  final int rent_day_7 = 7, rent_day_10 = 10, rent_day_20 = 20, rent_day_30 = 30;
  int current_rent_day;
  int rent_price_percent_7, rent_price_percent_10, rent_price_percent_20, rent_price_percent_30;
  boolean is_ready_rent_types = false;

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_rent);


    setupViews();
    setTypeFace();


    Bundle extras = getIntent().getExtras();
    prepareGame(extras ,savedInstanceState);





    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityShowRent.this.finish();
      }
    });


    btn_comments.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ActivityShowRent.this, ActivityComment.class);
        intent.putExtra("GAME_INFO_ID", game.getGame_info_id());
        intent.putExtra("GAME_NAME", game.getName());
        ActivityShowRent.this.startActivity(intent);
      }
    });



    btn_rent_day_count_7.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(is_ready_rent_types){
          setRentDay(rent_day_7);
        }
      }
    });

    btn_rent_day_count_10.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(is_ready_rent_types){
          setRentDay(rent_day_10);
        }
      }
    });

    btn_rent_day_count_20.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(is_ready_rent_types){
          setRentDay(rent_day_20);
        }
      }
    });

    btn_rent_day_count_30.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(is_ready_rent_types){
          setRentDay(rent_day_30);
        }
      }
    });


  }

  private void setupViews(){
    img_back = (ImageView) findViewById(R.id.img_back);
    txt_page_name = (TextView) findViewById(R.id.txt_page_name);

    app_bar = (AppBarLayout) findViewById(R.id.app_bar);
    collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    toolbar = (Toolbar) findViewById(R.id.toolbar);

    vdo_game = (VideoView) findViewById(R.id.vdo_game);
    img_game = (ImageView) findViewById(R.id.img_game);
    img_game_cover = (RoundedImageView) findViewById(R.id.img_game_cover);
    txt_name = (TextView) findViewById(R.id.txt_name);
    txt_console = (TextView) findViewById(R.id.txt_console);
    txt_genres = (TextView) findViewById(R.id.txt_genres);
    txt_show_genres = (TextView) findViewById(R.id.txt_show_genres);
    txt_release_date = (TextView) findViewById(R.id.txt_release_date);
    txt_rate = (TextView) findViewById(R.id.txt_rate);
    img_rate_star = (ImageView) findViewById(R.id.img_rate_star);
    txt_region = findViewById(R.id.txt_region);
    txt_show_region = findViewById(R.id.txt_show_region);

    //bottom of detail
    txt_detail_age_class = (TextView) findViewById(R.id.txt_detail_age_class);
    img_detail_console_icon = findViewById(R.id.img_detail_console_icon);
    txt_detail_release_date = findViewById(R.id.txt_detail_release_date);

    expand_text_view = (ExpandableTextView) findViewById(R.id.expand_text_view);

    nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
    txt_rent_period = (TextView) findViewById(R.id.txt_rent_period);
    btn_rent = findViewById(R.id.btn_rent);
    btn_rent_day_count_7 = findViewById(R.id.btn_rent_day_count_7);
    btn_rent_day_count_10 = findViewById(R.id.btn_rent_day_count_10);
    btn_rent_day_count_20 = findViewById(R.id.btn_rent_day_count_20);
    btn_rent_day_count_30 = findViewById(R.id.btn_rent_day_count_30);

    btn_comments = findViewById(R.id.btn_comments);

  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    txt_name.setTypeface(MyViews.getIranSansMediumFont(ActivityShowRent.this));
    txt_rate.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    txt_region.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    txt_show_region.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    txt_show_genres.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    txt_genres.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));

    txt_rent_period.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    btn_rent.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
    btn_rent_day_count_7.setTypeface(MyViews.getIranSansBoldFont(ActivityShowRent.this));
    btn_rent_day_count_10.setTypeface(MyViews.getIranSansBoldFont(ActivityShowRent.this));
    btn_rent_day_count_20.setTypeface(MyViews.getIranSansBoldFont(ActivityShowRent.this));
    btn_rent_day_count_30.setTypeface(MyViews.getIranSansBoldFont(ActivityShowRent.this));

    btn_comments.setTypeface(MyViews.getIranSansLightFont(ActivityShowRent.this));
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
          getRentTypes();
//          fillViews();
          setRelatedFragments();
        }
      }
    });

  }

  private void getRentTypes(){
    RentService service = new RentService(ActivityShowRent.this);
    service.getRentTypes(new RentService.onRentTypesReceived() {
      @Override
      public void onReceived(int status, String message, List<RentType> rentTypes) {
        if (status == 1){
          ActivityShowRent.this.rentTypes = rentTypes;
          initializeRentPricePercent();
          fillViews();
          setRentDay(rent_day_7);
          is_ready_rent_types = true;
        }else {
          MyViews.makeText(ActivityShowRent.this, message, Toast.LENGTH_SHORT);
        }
      }
    });
  }




  private void fillViews(){
    txt_page_name.setText(game.getName());
    txt_name.setText(game.getName());
    txt_console.setText(game.getConsole_name());

    String genres = "";
    for(int i=0 ; i<game.getGenres().size() ; i++){
      genres += game.getGenres().get(i);
    }
    txt_genres.setText(genres);
    txt_release_date.setText("تاریخ عرضه: " + game.getProduction_date());
//    txt_rate.setText("rate : 4.6");
    txt_region.setText(game.getRegion());

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




    //game detail
    expand_text_view.setText(game.getDescription());
    txt_detail_age_class.setText("+"+game.getAge_class());
    if(game.getConsole_name().contains("xbox")){
      img_detail_console_icon.setImageResource(R.drawable.ic_xbox);
    }else if(game.getConsole_name().contains("ps")){
      img_detail_console_icon.setImageResource(R.drawable.ic_playstation);
    }

    txt_detail_release_date.setText(game.getProduction_date().substring(0,4));

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
        mediaPlayer.setVolume(0,0);
        vdo_game.start();
      }
    });
    vdo_game.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.setVolume(0,0);
        vdo_game.start();
      }
    });
  }

  private void setRelatedFragments(){
    ft = getSupportFragmentManager().beginTransaction();
//    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
    FragmentRelatedRents relatedRents = new FragmentRelatedRents();
    Bundle args = new Bundle();
    args.putInt("game_id", game.getId());
    relatedRents.setArguments(args);


    FragmentRelatedPost relatedPost = new FragmentRelatedPost();
    Bundle args2 = new Bundle();
    args2.putInt("game_info_id", game.getGame_info_id());
    relatedPost.setArguments(args2);


    ft.replace(R.id.lyt_related_rents, relatedRents);
    ft.replace(R.id.lyt_related_posts, relatedPost);
    ft.commit();
  }


  @SuppressLint("SetTextI18n")
  private void setRentDay(int day){
    resetChoose();

    if(game.getCount() == 0){
      btn_rent.setBackgroundResource(R.drawable.back_list_game_price_finished);
      btn_rent.setText("ناموجود");
      return;
    }

    current_rent_day = day;
    switch (day){
      case rent_day_7 :
        btn_rent_day_count_7.setBackgroundResource(R.drawable.back_rent_day_count_ch);
        btn_rent.setText(" اجاره با قیمت " + HelperText.split_price(game.getPrice() * rent_price_percent_7 / 100) + " تومان ");
        break;

      case rent_day_10 :
        btn_rent_day_count_10.setBackgroundResource(R.drawable.back_rent_day_count_ch);
        btn_rent.setText(" اجاره با قیمت " + HelperText.split_price(game.getPrice() * rent_price_percent_10 / 100) + " تومان ");
        break;

      case rent_day_20 :
        btn_rent_day_count_20.setBackgroundResource(R.drawable.back_rent_day_count_ch);
        btn_rent.setText(" اجاره با قیمت " + HelperText.split_price(game.getPrice() * rent_price_percent_20 / 100) + " تومان ");
        break;

      case rent_day_30 :
        btn_rent_day_count_30.setBackgroundResource(R.drawable.back_rent_day_count_ch);
        btn_rent.setText(" اجاره با قیمت " + HelperText.split_price(game.getPrice() * rent_price_percent_30 / 100) + " تومان ");
        break;

    }
  }

  private void resetChoose(){
    btn_rent_day_count_7.setBackgroundResource(R.drawable.back_rent_day_count);
    btn_rent_day_count_10.setBackgroundResource(R.drawable.back_rent_day_count);
    btn_rent_day_count_20.setBackgroundResource(R.drawable.back_rent_day_count);
    btn_rent_day_count_30.setBackgroundResource(R.drawable.back_rent_day_count);
  }


  private void initializeRentPricePercent(){
    for (int i=0;i<rentTypes.size();i++){
      switch (rentTypes.get(i).getDay_count()){
        case 7 :
          rent_price_percent_7 = rentTypes.get(i).getPrice_percent();
          break;

        case 10:
          rent_price_percent_10 = rentTypes.get(i).getPrice_percent();
          break;

        case 20:
          rent_price_percent_20 = rentTypes.get(i).getPrice_percent();
          break;

        case 30:
          rent_price_percent_30 = rentTypes.get(i).getPrice_percent();
          break;
      }
    }
  }
}
