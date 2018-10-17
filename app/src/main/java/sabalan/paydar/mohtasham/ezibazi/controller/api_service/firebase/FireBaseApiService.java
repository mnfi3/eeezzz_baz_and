package sabalan.paydar.mohtasham.ezibazi.controller.api_service.firebase;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.controller.system.auth.Auth;
import sabalan.paydar.mohtasham.ezibazi.model.Comment;
import sabalan.paydar.mohtasham.ezibazi.model.Device;

public class FireBaseApiService {
  private Context context;
  public FireBaseApiService(Context context){
    this.context = context;
  }




  public void refreshFcmToken(JSONObject object, final onRefreshTokenReceived onRefreshTokenReceived){
    String url = Urls.REFRESH_FCM_TOKEN;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        Device device;
        try {
          status = response.getInt("status");
          message = response.getString("message");
          device = Device.Parser.parse(response.getJSONObject("data"));
          onRefreshTokenReceived.onReceived(status, message, device);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRefreshTokenReceived.onReceived(0, "خطا در اتصال به سرور", new Device());
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Authorization", "Bearer " + Auth.getUserToken());
        return params;
      }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }







  public interface onRefreshTokenReceived{
    void onReceived(int status, String message, Device device);
  }


}
