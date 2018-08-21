package mohtasham.paydar.sabalan.ezbazi.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;
import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.MainSliderService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityMain;


public class FragmentMainSlider extends Fragment {

  Context context = getContext();
  SliderLayout sliderLayout;
  TextView textView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_slider, container, false);
    sliderLayout = view.findViewById(R.id.slider);
    textView = view.findViewById(R.id.txt_test);
    textView.setText("hello");
    setSlider();
    return view;
  }


  private void setSlider(){
    MainSliderService apiService = new MainSliderService(G.context);
    apiService.getMainSlider(new MainSliderService.onSliderReceived() {
      @Override
      public void onReceived(String message, List<MainSlider> sliders) {
        for (int i=0 ; i<sliders.size() ; i++){
          TextSliderView textSliderView = new TextSliderView(G.context);
          textSliderView
            .description(sliders.get(i).getTitle())
            .image(sliders.get(i).getImage_url())
            .setScaleType(BaseSliderView.ScaleType.Fit);
          textSliderView
            .bundle(new Bundle())
            .getBundle().putInt("extra", sliders.get(i).getId());

          sliderLayout.addSlider(textSliderView);
        }


      }
  });




    /*HashMap < String, String > url_maps = new HashMap<String, String>();
    url_maps.put("1", "http://192.168.10.83/izi-bazi.ud/uploads/images/slide1.jpg");
    url_maps.put("2", "http://192.168.10.83/izi-bazi.ud/uploads/images/slide2.jpg");
    url_maps.put("3", "http://192.168.10.83/izi-bazi.ud/uploads/images/slide3.jpg");
    url_maps.put("4", "http://192.168.10.83/izi-bazi.ud/uploads/images/slide4.jpg");
    url_maps.put("5", "http://192.168.10.83/izi-bazi.ud/uploads/images/slide5.jpg");



    for(String name : url_maps.keySet()){
      TextSliderView textSliderView = new TextSliderView(G.context);
      // initialize a SliderLayout
      textSliderView
         .description(name)
        .image(url_maps.get(name));
       //.setScaleType(BaseSliderView.ScaleType.Fit);
      //.setOnSliderClickListener(this);

      //add your extra information
//      textSliderView.bundle(new Bundle());
//      textSliderView.getBundle()
//        .putString("extra",name);

      sliderLayout.addSlider(textSliderView);
    }*/


  }


}
