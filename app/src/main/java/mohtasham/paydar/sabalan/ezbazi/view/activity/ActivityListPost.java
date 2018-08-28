package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.PostListAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;

public class ActivityListPost extends AppCompatActivity {

  RecyclerView rcv_list_post;
  PostService apiService;
  int page_num = 1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_post);

    setupViews();

    rcv_list_post.setLayoutManager(new GridLayoutManager(G.context,1,GridLayoutManager.VERTICAL,false));
    getPosts();
  }

  private void setupViews(){
    rcv_list_post = (RecyclerView) findViewById(R.id.rcv_list_post);
  }



  private void getPosts(){
    apiService = new PostService(G.context);
    apiService.getMainPosts(page_num, new PostService.onPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts, Paginate paginate) {
        if(status == 0){
//          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
        }else {
          PostListAdapter listAdapter = new PostListAdapter(G.context, posts);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(listAdapter);
          rcv_list_post.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
      }
    });
  }


}
