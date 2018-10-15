package sabalan.paydar.mohtasham.ezibazi.controller.api_service.main;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.MainSlider;

public class SliderMainService {
  private Context context;
  public SliderMainService(Context context){
    this.context = context;
  }


  public void getMainSlider(final onSliderReceived onSliderReceived){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.MAIN_SLIDER, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<MainSlider> sliders = new ArrayList<>();
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          if (status == 0){
            onSliderReceived.onReceived(message, sliders);
          }else {
            //parse json object
            JSONArray data = response.getJSONArray("data");
            for (int i=0 ; i<data.length() ; i++){
              JSONObject sliderObject = data.getJSONObject(i);
              sliders.add(MainSlider.Parser.parse(sliderObject));
            }
            onSliderReceived.onReceived(message, sliders);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSliderReceived.onReceived("", new ArrayList<MainSlider>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }

  public interface onSliderReceived{
    void onReceived(String message, List<MainSlider> sliders);
  }


}
