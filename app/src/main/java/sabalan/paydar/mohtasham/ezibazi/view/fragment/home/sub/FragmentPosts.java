package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.adapter.recyclerview.PostMainAdapter;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.PostService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.Post;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityListPost;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentPosts extends Fragment {

  LinearLayout lyt_posts;
  RecyclerView rcv_posts;
  PostService apiService;
  int page_num = 1;
  TextView txt_show_posts;
  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main_posts, container, false);

    setupViews();
    setTypeFace();
    lyt_posts.setVisibility(View.INVISIBLE);

    rcv_posts.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));
    getPosts();

    txt_show_posts.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(G.context, ActivityListPost.class);
        startActivity(intent);
      }
    });

    return view;
  }


  private void setupViews() {
    rcv_posts = view.findViewById(R.id.rcv_posts);
    txt_show_posts = view.findViewById(R.id.txt_show_posts);
    lyt_posts = view.findViewById(R.id.lyt_posts);
  }

  private void getPosts(){
    apiService = new PostService(G.context);
    apiService.getMainPosts(page_num, new PostService.onPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts, Paginate paginate) {
        if(status == 0){
//          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
        }else {
          PostMainAdapter listAdapter = new PostMainAdapter(getActivity(), posts);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
          rcv_posts.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
          setAnimation();
          lyt_posts.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_left);
    lyt_posts.setAnimation(anim);
    anim.start();
  }


  private void setTypeFace(){
    txt_show_posts.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }






}