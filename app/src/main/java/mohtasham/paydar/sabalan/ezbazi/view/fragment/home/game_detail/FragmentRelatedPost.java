package mohtasham.paydar.sabalan.ezbazi.view.fragment.home.game_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.PostMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.RentMainAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentRelatedPost extends Fragment {

  int game_info_id;
  public void setId(int game_info_id){
    this.game_info_id = game_info_id;
  }



  private static final String TAG = "FragmentRelatedPost";

  LinearLayout lyt_related_posts;
  RecyclerView rcv_related_posts;
  PostService apiService;
  TextView txt_related_posts;

  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_related_post, container, false);
    setupViews();
    setTypeFace();
    lyt_related_posts.setVisibility(View.GONE);

    rcv_related_posts.setLayoutManager((new LinearLayoutManager(G.context,LinearLayoutManager.HORIZONTAL,true)));

    game_info_id = getArguments().getInt("game_info_id");
    getRelatedPosts();

    return view;
  }

  private void setupViews(){
    rcv_related_posts = view.findViewById(R.id.rcv_related_posts);
    txt_related_posts = view.findViewById(R.id.txt_related_posts);
    lyt_related_posts = view.findViewById(R.id.lyt_related_posts);
  }

  private void setTypeFace(){
    txt_related_posts.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }

  private void getRelatedPosts(){
    apiService = new PostService(getContext());
    apiService.getRelatedPosts(game_info_id, new PostService.onRelatedPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts) {
        if (status == 0) {
          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        } else {
          if(posts.size() > 0) {
            PostMainAdapter listAdapter = new PostMainAdapter(getActivity(), posts);
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
            rcv_related_posts.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
            setAnimation();
            lyt_related_posts.setVisibility(View.VISIBLE);
          }
        }
      }
    });
  }

  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right);
    lyt_related_posts.setAnimation(anim);
    anim.start();
  }

}
