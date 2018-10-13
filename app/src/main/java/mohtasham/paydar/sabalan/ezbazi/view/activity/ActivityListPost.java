package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.List;
import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListPostAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.application.G;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListPost extends AppCompatActivity {


  ImageView img_back;
  TextView txt_page_name;

  RecyclerView rcv_list_post;
  PostService apiService;


  AVLoadingIndicatorView avl_center, avl_bottom;
  int page_num = 1;
  Paginate paginate;
  ListPostAdapter listAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_post);

    setupViews();
    setTypeFace();

    txt_page_name.setText("جدیدترین اخبار");

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityListPost.this.finish();
      }
    });


      rcv_list_post.setLayoutManager(new GridLayoutManager(ActivityListPost.this, 1, GridLayoutManager.VERTICAL, false));
    getPosts();


    rcv_list_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1)) {
          if (paginate != null) {
            if (page_num != paginate.getLast_page()){
              page_num++;
              avl_bottom.setVisibility(View.VISIBLE);
              getPosts();
            }
          }
        }
      }
    });


  }

  private void setupViews(){
    img_back = findViewById(R.id.img_back);
    txt_page_name = findViewById(R.id.txt_page_name);
    rcv_list_post = findViewById(R.id.rcv_list_post);
    avl_center = findViewById(R.id.avl_center);
    avl_bottom = findViewById(R.id.avl_bottom);
  }

  private void setTypeFace(){
    txt_page_name.setTypeface(MyViews.getIranSansLightFont(ActivityListPost.this));
  }



  private void getPosts(){
    apiService = new PostService(ActivityListPost.this);
    apiService.getMainPosts(page_num, new PostService.onPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts, Paginate paginate) {
        avl_center.setVisibility(View.INVISIBLE);
        avl_bottom.setVisibility(View.INVISIBLE);
        if(status == 0){
//          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
        }else {
          ActivityListPost.this.paginate = paginate;
          if(page_num == 1) {
            listAdapter = new ListPostAdapter(ActivityListPost.this, posts);
            SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
            rcv_list_post.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
          }else {
            listAdapter.notifyData(posts);
          }
        }
      }
    });
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
