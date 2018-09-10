package mohtasham.paydar.sabalan.ezbazi.view.fragment.main_menu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu.SliderMainService;
import mohtasham.paydar.sabalan.ezbazi.controller.system.G;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;


public class FragmentSlider extends Fragment {

  SliderLayout sliderLayout;
  CoordinatorLayout lyt_slider;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_slider, container, false);
    sliderLayout = view.findViewById(R.id.slider);
    lyt_slider = view.findViewById(R.id.lyt_slider);
    getSlider();

    return view;
  }


  private void setSlider(List<MainSlider> sliders){
    for (int i=0;i<sliders.size();i++){
      TextSliderView textSliderView = new TextSliderView(G.context);
     // final int finalI = i;
      textSliderView
        .description(sliders.get(i).getTitle())
        .image(sliders.get(i).getImage_url())
        .setScaleType(BaseSliderView.ScaleType.Fit)
        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
          @Override
          public void onSliderClick(BaseSliderView slider) {
           // Toast.makeText(G.context, "slider " + sliders.get(finalI).getId(), Toast.LENGTH_SHORT).show();
          }
        });

//          textSliderView
//            .bundle(new Bundle())
//            .getBundle().putInt("extra", sliders.get(i).getId());

      sliderLayout.addSlider(textSliderView);
    }

    sliderLayout.startAutoCycle();
    sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    sliderLayout.setCustomAnimation(new DescriptionAnimation());
    sliderLayout.setDuration(6000);

  }

  private void getSlider(){
    SliderMainService apiService = new SliderMainService(G.context);
    apiService.getMainSlider(new SliderMainService.onSliderReceived() {
      @Override
      public void onReceived(String message, final List<MainSlider> sliders) {
        setSlider(sliders);
//        setAnimation();
      }
    });
  }


  private void setAnimation(){
    Animation anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right);
    lyt_slider.setAnimation(anim);
    anim.start();
  }

}
