package sabalan.paydar.mohtasham.ezibazi.view.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.api_service.user_actvity.UserRequestsService;
import sabalan.paydar.mohtasham.ezibazi.model.RentRequest;
import sabalan.paydar.mohtasham.ezibazi.model.ShopRequest;
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.user_activity.ListRentRequestAdapter;
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.user_activity.ListShopRequestAdapter;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter;


public class FragmentActivity extends Fragment {

  private static final String TAG = "FragmentActivity";

  TextView txt_rents, txt_buys;
  RecyclerView rcv_rents, rcv_buys;
  UserRequestsService service;
  AVLoadingIndicatorView avl_rents, avl_buys;

  View view;
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_activity, container, false);
//    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
    setupViews();
    setTypeFace();

    rcv_rents.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,true)));
    rcv_buys.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,true)));

    getUserRentRequests();
    avl_rents.setVisibility(View.VISIBLE);
    getUserShopRequests();
    avl_buys.setVisibility(View.VISIBLE);




    return view;
  }

  private void setupViews(){
    txt_rents = view.findViewById(R.id.txt_rents);
    txt_buys = view.findViewById(R.id.txt_buys);
    rcv_rents = view.findViewById(R.id.rcv_rents);
    rcv_buys = view.findViewById(R.id.rcv_buys);
    avl_rents = view.findViewById(R.id.avl_rents);
    avl_buys = view.findViewById(R.id.avl_buys);
  }

  private void setTypeFace(){
    txt_rents.setTypeface(MyViews.getIranSansMediumFont(getContext()));
    txt_buys.setTypeface(MyViews.getIranSansMediumFont(getContext()));
  }

  private void getUserRentRequests(){
    service = new UserRequestsService(getContext());
    service.getRentRequests(new UserRequestsService.onRentRequestsReceived() {
      @Override
      public void onReceived(int status, String message, List<RentRequest> requests) {
        avl_rents.setVisibility(View.INVISIBLE);
        if(status != 0){
          ListRentRequestAdapter adapter = new ListRentRequestAdapter(getContext(),requests);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
          rcv_rents.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
        }else {
//          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        }
      }
    });
  }

  private void getUserShopRequests(){
    service = new UserRequestsService(getContext());
    service.getShopRequests(new UserRequestsService.onShopRequestsReceived() {
      @Override
      public void onReceived(int status, String message, List<ShopRequest> requests) {
        avl_buys.setVisibility(View.INVISIBLE);
        if(status == 1){
          ListShopRequestAdapter adapter = new ListShopRequestAdapter(getContext(),requests);
          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
          rcv_buys.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
        }else {
//          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
        }
      }
    });
  }


  @Override
  public void onStart() {
    super.onStart();

//    getUserRentRequests();
//    getUserShopRequests();
  }

  @Override
  public void onResume() {
    super.onResume();

//    getUserRentRequests();
//    getUserShopRequests();
  }
}
