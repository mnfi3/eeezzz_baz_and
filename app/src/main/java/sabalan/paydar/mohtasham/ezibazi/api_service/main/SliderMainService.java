package sabalan.paydar.mohtasham.ezibazi.api_service.main;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.MainSlider;

public class SliderMainService {
  private Context context;
  public SliderMainService(Context context){
    this.context = context;
  }


  public void getMainSlider(final onSliderReceived onSliderReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.MAIN_SLIDER, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSliderReceived.onReceived(message, new ArrayList<MainSlider>());
          return;
        }

        List<MainSlider> sliders = new ArrayList<>();
        try {
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject sliderObject = data.getJSONObject(i);
            sliders.add(MainSlider.Parser.parse(sliderObject));
          }

          onSliderReceived.onReceived(message, sliders);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });



  }

  public interface onSliderReceived{
    void onReceived(String message, List<MainSlider> sliders);
  }


}
