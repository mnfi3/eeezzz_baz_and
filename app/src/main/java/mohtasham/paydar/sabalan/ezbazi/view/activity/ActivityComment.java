package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListCommentAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.comment.CommentService;
import mohtasham.paydar.sabalan.ezbazi.model.Comment;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityComment extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  RecyclerView rcv_comments;

  CommentService service;

  int page_num = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comment);

    setupViews();
    setTypeFace();

    rcv_comments.setLayoutManager(new GridLayoutManager(ActivityComment.this,1, GridLayoutManager.VERTICAL,false));

    Bundle extras = getIntent().getExtras();
    prepareComments(extras, savedInstanceState);
  }

  private void setupViews(){
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
    rcv_comments = findViewById(R.id.rcv_comments);

  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityComment.this));
  }

  private void prepareComments(Bundle extras, Bundle savedInstanceState) {
    int game_info_id;
    if (extras != null) {
      game_info_id = extras.getInt("GAME_INFO_ID");
    } else {
      game_info_id = (int) savedInstanceState.getSerializable("GAME_INFO_ID");
    }

    service = new CommentService(ActivityComment.this);
    service.getComments(game_info_id, page_num, new CommentService.onCommentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Comment> comments, Paginate paginate) {
        if(status != 1){
          MyViews.makeText(ActivityComment.this, message, Toast.LENGTH_SHORT);
          return;
        }
        if(comments.size() > 0){
          ListCommentAdapter adapter = new ListCommentAdapter(ActivityComment.this, comments);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_comments.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
      }
    });

//    service = new RentService(ActivityShowRent.this);
//    service.getSpecialRent(game_info_id, new RentService.onSpecialRentReceived() {
//      @Override
//      public void onReceived(int status, String message, Game game) {
//        if (status != 1) {
//          MyViews.makeText(ActivityShowRent.this, message, Toast.LENGTH_SHORT);
//        } else {
//          ActivityShowRent.this.game = game;
//          fillViews();
//          setRelatedFragments();
//        }
//      }
//    });
  }
}
