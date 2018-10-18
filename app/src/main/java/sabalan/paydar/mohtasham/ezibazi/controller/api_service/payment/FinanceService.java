package sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment;

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

public class FinanceService {
  private Context context;
  public FinanceService(Context context){
    this.context = context;
  }


  public void getPayUrl(JSONObject jsonObject, final onCreditComplete onCreditComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.INCREASE_CREDIT, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String url = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          url = response.getString("data");
          onCreditComplete.onComplete(status, message, url);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onCreditComplete.onComplete(0, "خطا در ارتباط با سرور", "");
      }
    })
    {
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










  public interface onCreditComplete{
    void onComplete(int status, String message, String url);
  }



}
