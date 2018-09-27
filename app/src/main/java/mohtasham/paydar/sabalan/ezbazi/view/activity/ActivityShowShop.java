package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.ShopService;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail.FragmentRelatedPost;
import mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail.FragmentRelatedShops;


public class ActivityShowShop extends AppCompatActivity {
  private static final String TAG = "ActivityShowRent";

  ImageView img_back;
  TextView txt_page_name;
  AppBarLayout app_bar;
  CollapsingToolbarLayout collapsing_toolbar;
  Toolbar toolbar;

  VideoView vdo_game;
  ImageView img_game;
  RoundedImageView img_game_cover;
  TextView txt_name, txt_console, txt_genres, txt_release_date, txt_rate;
  ImageView img_rate_star;
  ExpandableTextView expand_text_view;

  Game game;

  ShopService service;

  NestedScrollView nested_scroll;
  TextView txt_rent_period;
  Button btn_rent;
  Button btn_rent_day_count_10, btn_rent_day_count_20, btn_rent_day_count_30;
  Button btn_comments;

  FragmentTransaction ft;



  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_shop);

    setupViews();
    setTypeFace();

    Bundle extras = getIntent().getExtras();
    prepareGame(extras ,savedInstanceState);


    nested_scroll.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
          case MotionEvent.ACTION_DOWN:
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;

          case MotionEvent.ACTION_UP:
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;
        }
        // Handle nestedScrollView touch events.
        v.onTouchEvent(event);
        return true;
      }
    });


    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityShowShop.this.finish();
      }
    });


    btn_comments.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ActivityShowShop.this, ActivityComment.class);
        intent.putExtra("GAME_INFO_ID", game.getId());
        ActivityShowShop.this.startActivity(intent);
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
    txt_release_date = (TextView) findViewById(R.id.txt_release_date);
    txt_rate = (TextView) findViewById(R.id.txt_rate);
    img_rate_star = (ImageView) findViewById(R.id.img_rate_star);

    expand_text_view = (ExpandableTextView) findViewById(R.id.expand_text_view);

    nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
    txt_rent_period = (TextView) findViewById(R.id.txt_rent_period);
    btn_rent = findViewById(R.id.btn_rent);
    btn_rent_day_count_10 = findViewById(R.id.btn_rent_day_count_10);
    btn_rent_day_count_20 = findViewById(R.id.btn_rent_day_count_20);
    btn_rent_day_count_30 = findViewById(R.id.btn_rent_day_count_30);

    btn_comments = findViewById(R.id.btn_comments);

  }

  private void prepareGame(Bundle extras, Bundle savedInstanceState){
    int id ;
    if(extras != null) {
     id = extras.getInt("ID");
    }else {
     id = (int) savedInstanceState.getSerializable("ID");
    }

    service = new ShopService(ActivityShowShop.this);
    service.getSpecialShop(id, new ShopService.onSpecialShopReceived() {
      @Override
      public void onReceived(int status, String message, Game game) {
        if(status != 1){
          MyViews.makeText(ActivityShowShop.this, message, Toast.LENGTH_SHORT);
        }else {
          ActivityShowShop.this.game = game;
          fillViews();
          setRelatedFragments();
        }
      }
    });

  }


  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityShowShop.this));
    txt_name.setTypeface(MyViews.getIranSansMediumFont(ActivityShowShop.this));
    txt_rate.setTypeface(MyViews.getIranSansLightFont(ActivityShowShop.this));

    txt_rent_period.setTypeface(MyViews.getIranSansLightFont(ActivityShowShop.this));
    btn_rent.setTypeface(MyViews.getIranSansLightFont(ActivityShowShop.this));
    btn_rent_day_count_10.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));
    btn_rent_day_count_20.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));
    btn_rent_day_count_30.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));
    
    btn_comments.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));


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
    txt_rate.setText("rate : 4.6");

    String cover_image = "";
    for (int i=0 ; i<game.getPhotos().size() ; i++){
//      if (game.getPhotos().get(i).getHeight() == R.dimen.photo_cover_height){
        cover_image = game.getPhotos().get(0).getUrl();
        break;
//      }
    }
    Picasso.with(ActivityShowShop.this).
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

    expand_text_view.setText(game.getDescription());
  }

  private void setRelatedFragments(){
    ft = getSupportFragmentManager().beginTransaction();
//    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
    FragmentRelatedShops relatedShops = new FragmentRelatedShops();
    Bundle args = new Bundle();
    args.putInt("game_id", game.getId());
    relatedShops.setArguments(args);
//    relatedRents.setId(game.getId());

    FragmentRelatedPost relatedPost = new FragmentRelatedPost();
    Bundle args2 = new Bundle();
    args2.putInt("game_info_id", game.getGame_info_id());
    relatedPost.setArguments(args2);


    ft.replace(R.id.lyt_related_shops, relatedShops);
    ft.replace(R.id.lyt_related_posts, relatedPost);
    ft.commit();
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
