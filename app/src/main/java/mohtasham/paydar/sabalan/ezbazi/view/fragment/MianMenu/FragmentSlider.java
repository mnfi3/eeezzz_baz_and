package mohtasham.paydar.sabalan.ezbazi.view.fragment.MianMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.SliderMainService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;


public class FragmentSlider extends Fragment {

  SliderLayout sliderLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_slider, container, false);
    sliderLayout = view.findViewById(R.id.slider);
    setSlider();

    return view;
  }


  private void setSlider(){
    SliderMainService apiService = new SliderMainService(G.context);
    apiService.getMainSlider(new SliderMainService.onSliderReceived() {
      @Override
      public void onReceived(String message, final List<MainSlider> sliders) {
        for (int i=0 ; i<sliders.size() ; i++){
          TextSliderView textSliderView = new TextSliderView(G.context);
          final int finalI = i;
          textSliderView
            .description(sliders.get(i).getTitle())
            .image(sliders.get(i).getImage_url())
            .setScaleType(BaseSliderView.ScaleType.Fit)
            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
              @Override
              public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(G.context, "slider " + sliders.get(finalI).getId(), Toast.LENGTH_SHORT).show();
              }
            });

//          textSliderView
//            .bundle(new Bundle())
//            .getBundle().putInt("extra", sliders.get(i).getId());


          sliderLayout.addSlider(textSliderView);
        }


      }
    });

  }


}
