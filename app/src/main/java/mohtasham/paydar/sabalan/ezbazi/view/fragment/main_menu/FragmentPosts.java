package mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.PostMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentPosts extends Fragment {

  RecyclerView rcv_posts;
  PostService apiService;
  int page_num = 1;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_posts, container, false);

    rcv_posts = view.findViewById(R.id.rcv_posts);
    rcv_posts.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));
    getPosts();

    return view;
  }


  private void getPosts(){
    apiService = new PostService(G.context);
    apiService.getMainPosts(page_num, new PostService.onPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts, Paginate paginate) {
        if(status == 0){
//          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
        }else {
          PostMainAdapter listAdapter=new PostMainAdapter(G.context, posts);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          rcv_posts.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
        }
      }
    });
  }






}
