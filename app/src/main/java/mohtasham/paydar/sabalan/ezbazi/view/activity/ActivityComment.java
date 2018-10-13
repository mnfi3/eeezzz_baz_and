package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListCommentAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.comment.CommentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.application.G;
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
  FloatingActionButton fab_add_comment;

  Dialog dialog;
  Button btn_add_comment;
  TextView txt_dialog_head;
  EditText edt_comment;

  int game_info_id;


  AVLoadingIndicatorView avl_center, avl_bottom;
  int page_num = 1;
  Paginate paginate;
  ListCommentAdapter adapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comment);

    setupViews();
    setTypeFace();

    rcv_comments.setLayoutManager(new GridLayoutManager(ActivityComment.this,1, GridLayoutManager.VERTICAL,false));

    Bundle extras = getIntent().getExtras();
    prepareComments(extras, savedInstanceState);


    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });


    fab_add_comment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!G.isLoggedIn){
          Intent intent = new Intent(ActivityComment.this, ActivityLogin.class);
          startActivity(intent);
          finish();
        }else {
          dialog.show();
        }
      }
    });



      btn_add_comment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          insertComment();
          dialog.dismiss();
        }
      });


    rcv_comments.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1)) {
          if (paginate != null) {
            if (page_num != paginate.getLast_page()){
              page_num++;
              avl_bottom.setVisibility(View.VISIBLE);
              getComments();
            }
          }
        }
      }
    });


  }

  private void setupViews(){
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
    rcv_comments = findViewById(R.id.rcv_comments);
    fab_add_comment = findViewById(R.id.fab_add_comment);


    //dialog
    dialog = new Dialog(ActivityComment.this);
    dialog.setContentView(R.layout.custom_dialog_insert_comment);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    btn_add_comment = dialog.findViewById(R.id.btn_add_comment);
    txt_dialog_head = dialog.findViewById(R.id.txt_dialog_head);
    edt_comment = dialog.findViewById(R.id.edt_comment);

    avl_center = findViewById(R.id.avl_center);
    avl_bottom = findViewById(R.id.avl_bottom);

  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityComment.this));

    //dialog
    btn_add_comment.setTypeface(MyViews.getIranSansLightFont(ActivityComment.this));
    txt_dialog_head.setTypeface(MyViews.getIranSansLightFont(ActivityComment.this));
    edt_comment.setTypeface(MyViews.getIranSansLightFont(ActivityComment.this));

  }

  private void prepareComments(Bundle extras, Bundle savedInstanceState) {
    int game_info_id;
    String game_name = "";
    if (extras != null) {
      game_info_id = extras.getInt("GAME_INFO_ID");
      game_name = extras.getString("GAME_NAME");
    } else {
      game_info_id = (int) savedInstanceState.getSerializable("GAME_INFO_ID");
      game_name = savedInstanceState.getString("GAME_NAME");
    }
    this.game_info_id = game_info_id;

    txt_page_name.setText(" نظرات " + game_name);

    getComments();



  }


  private void getComments(){
    service = new CommentService(ActivityComment.this);
    service.getComments(game_info_id, page_num, new CommentService.onCommentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Comment> comments, Paginate paginate) {
        avl_center.setVisibility(View.INVISIBLE);
        avl_bottom.setVisibility(View.INVISIBLE);
        if(status != 1){
//          MyViews.makeText(ActivityComment.this, message, Toast.LENGTH_SHORT);
          return;
        }
        ActivityComment.this.paginate = paginate;
        if(page_num == 1){
          adapter = new ListCommentAdapter(ActivityComment.this, comments);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_comments.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }else {
          adapter.notifyData(comments);
        }
      }
    });
  }




  private void insertComment(){
    if(!checkEntry()){
      return;
    }
    String comment = edt_comment.getText().toString();
    JSONObject object = new JSONObject();
    try {
      object.put("commentable_id", game_info_id);
      object.put("commentable_type", "GameInfo");
      object.put("text", comment);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    service = new CommentService(ActivityComment.this);
    service.addComment(object, new CommentService.onAddCommentReceived() {
      @Override
      public void onReceived(int status, String message, Comment comment) {
        edt_comment.setText("");
        if (status == 1){
          MyViews.makeText(ActivityComment.this, "نظر شما با موفقیت ثبت شد", Toast.LENGTH_SHORT);
        }else {
          MyViews.makeText(ActivityComment.this, message, Toast.LENGTH_SHORT);
        }
      }
    });


  }

  private boolean checkEntry(){
    String text = edt_comment.getText().toString();
    if(text.length() < 3){
      MyViews.makeText(ActivityComment.this, "متن شما برای ثبت نظر بسیار کوتاه است", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }





  protected void onStart() {
    super.onStart();

//    connectivityListener = new ConnectivityListener(lyt_action);
    registerReceiver(G.connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(G.connectivityListener);
  }


}
