package mohtasham.paydar.sabalan.ezbazi.view.fragment.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListPostAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListRentAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview.ListShopAdapter;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.PostService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.RentService;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.ShopService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.controller.system.UserSharedPrefManager;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityListRent;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter;


public class FragmentSearch extends Fragment {


  EditText edt_search;

  TextView txt_search_in;
  RadioGroup rdg_search;
  RadioButton rdo_rent,rdo_shop,rdo_post;
  AVLoadingIndicatorView avl_search;
  TextView txt_no_result;
  RecyclerView rcv_search;
  ImageView img_search;

  View view;

  int rdo_rent_num = 1;
  int rdo_shop_num = 2;
  int rdo_post_num = 3;

  int current_rdo = rdo_rent_num;

  RentService rentService;
  ShopService shopService;
  PostService postService;
  UserSharedPrefManager prefManager;




  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_search, container, false);

    setupViews();
    setTypeFace();

    rcv_search.setLayoutManager(new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL,false));

    img_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        search();
      }
    });

    rdg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        boolean rdo_rent_check = rdo_rent.isChecked();
        boolean rdo_shop_check = rdo_shop.isChecked();
        boolean rdo_post_check = rdo_post.isChecked();
        if (rdo_rent_check) {
          current_rdo = rdo_rent_num;
//         Toast.makeText(getContext(), "rent",Toast.LENGTH_SHORT).show();
        } else if (rdo_shop_check) {
          current_rdo = rdo_shop_num;
//         Toast.makeText(getContext(), "shop",Toast.LENGTH_SHORT).show();
        } else if (rdo_post_check) {
          current_rdo = rdo_post_num;
//         Toast.makeText(getContext(), "post",Toast.LENGTH_SHORT).show();
        }
        if (edt_search.getText().toString().length() >= 3) {
          search(); } }});

    edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          search(); }return false; }});


    return view;
  }


  private void setupViews() {
    edt_search = view.findViewById(R.id.edt_search);
    txt_search_in = view.findViewById(R.id.txt_search_in);
    rdg_search = view.findViewById(R.id.rdg_search);
    rdo_rent = view.findViewById(R.id.rdo_rent);
    rdo_shop = view.findViewById(R.id.rdo_shop);
    rdo_post = view.findViewById(R.id.rdo_post);
    avl_search = view.findViewById(R.id.avl_search);
    txt_no_result = view.findViewById(R.id.txt_no_result);
    rcv_search = view.findViewById(R.id.rcv_search);
    img_search = view.findViewById(R.id.img_search);

  }

  private void setTypeFace() {
    edt_search.setTypeface(MyViews.getIranSansLightFont(getContext()));
    txt_search_in.setTypeface(MyViews.getIranSansLightFont(getContext()));
    rdo_rent.setTypeface(MyViews.getIranSansLightFont(getContext()));
    rdo_shop.setTypeface(MyViews.getIranSansLightFont(getContext()));
    rdo_post.setTypeface(MyViews.getIranSansLightFont(getContext()));
  }


  private void search() {
    if(checkEntry()) {
      prefManager = new UserSharedPrefManager(getContext());
      int city_id = prefManager.getCityId();
      String text = edt_search.getText().toString();
      JSONObject object = new JSONObject();
      try {
        object.put("text", text);
        object.put("city_id", city_id);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      txt_no_result.setVisibility(View.GONE);
      avl_search.setVisibility(View.VISIBLE);

      emptyRecyclerView();

      switch (current_rdo) {
        case 1:
          searchRents(object);
          break;

        case 2:
          searchShops(object);
          break;

        case 3:
          searchPosts(object);
          break;

      }
    }
  }

  private void searchRents(JSONObject object){
    rentService = new RentService(getContext());
    rentService.getSearchedRents(object, new RentService.onSearchedRentsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games) {
        avl_search.setVisibility(View.GONE);
        if(games.size() > 0) {
          ListRentAdapter adapter = new ListRentAdapter(getContext(), games);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_search.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }else {
          txt_no_result.setVisibility(View.VISIBLE);
        }
      }
    });

  }
  private void searchShops(JSONObject object){
    shopService = new ShopService(getContext());
    shopService.getSearchedShops(object, new ShopService.onSearchedShopsReceived() {
      @Override
      public void onReceived(int status, String message, List<Game> games) {
        avl_search.setVisibility(View.GONE);
        if(games.size() > 0) {
          ListShopAdapter adapter = new ListShopAdapter(getContext(), games);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_search.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }else {
          txt_no_result.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void searchPosts(JSONObject object){
    postService = new PostService(getContext());
    postService.getSearchedPosts(object, new PostService.onSearchedPostsReceived() {
      @Override
      public void onReceived(int status, String message, List<Post> posts) {
        avl_search.setVisibility(View.GONE);
        if(posts.size() > 0){
          ListPostAdapter adapter = new ListPostAdapter(getContext(), posts);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_search.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }else {
          txt_no_result.setVisibility(View.VISIBLE);
        }
      }
    });
  }



  private boolean checkEntry(){
    String text = edt_search.getText().toString();
    if(text.length() < 3){
      MyViews.makeText((AppCompatActivity) getActivity(), "متن شما برای جستجو بسیار کوتاه است", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }

  private void emptyRecyclerView(){
    ListShopAdapter adapter = new ListShopAdapter(getContext(), new ArrayList<Game>());
    rcv_search.setAdapter(adapter);
  }





}
