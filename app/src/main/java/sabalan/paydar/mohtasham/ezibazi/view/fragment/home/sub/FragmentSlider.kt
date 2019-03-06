package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.sub

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.SliderMainService
import sabalan.paydar.mohtasham.ezibazi.model.MainSlider
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityWebView


class FragmentSlider : Fragment() {

    internal lateinit var sliderLayout: SliderLayout
    internal lateinit var lyt_slider: CoordinatorLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_slider, container, false)
        sliderLayout = view.findViewById(R.id.slider)
        lyt_slider = view.findViewById(R.id.lyt_slider)
        getSlider()

        return view
    }


    private fun setSlider(sliders: List<MainSlider>) {
        for (i in sliders.indices) {
            val textSliderView = TextSliderView(context)
            textSliderView
                    .description(sliders[i].title)
                    .image(sliders[i].image_url)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener {
                        // Toast.makeText(G.context, "slider " + sliders.get(finalI).getId(), Toast.LENGTH_SHORT).show();
                        val intent = Intent(context, ActivityWebView::class.java)
                        intent.putExtra("URL", sliders[i].on_click)
                        startActivity(intent)
                    }


            //          textSliderView
            //            .bundle(new Bundle())
            //            .getBundle().putInt("extra", sliders.get(i).getId());

            sliderLayout.addSlider(textSliderView)
        }

        sliderLayout.startAutoCycle()
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage)
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderLayout.setCustomAnimation(DescriptionAnimation())
        sliderLayout.setDuration(6000)

    }

    private fun getSlider() {
        val apiService = SliderMainService(context!!)
        val onSliderReceived = object : SliderMainService.onSliderReceived{
            override fun onReceived(message: String, sliders: List<MainSlider>) {
                setSlider(sliders)
            }
        }
        apiService.getMainSlider(onSliderReceived)
    }


    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_right)
        lyt_slider.animation = anim
        anim.start()
    }

}