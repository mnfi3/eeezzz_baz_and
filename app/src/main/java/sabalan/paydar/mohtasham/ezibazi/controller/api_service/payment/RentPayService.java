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

public class RentPayService {
  private Context context;
  public RentPayService(Context context){
    this.context = context;
  }


  public void rentWithWallet(JSONObject jsonObject, final onRentWithWalletComplete onRentWithWalletComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.RENT_WITH_WALLET, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String url = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          onRentWithWalletComplete.onComplete(status, message);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRentWithWalletComplete.onComplete(0, "خطا در ارتباط با سرور");
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



  public void rentWithBank(JSONObject jsonObject, final onRentWithBankComplete onRentWithBankComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.RENT_WITH_BANK, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String url = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          onRentWithBankComplete.onComplete(status, message);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRentWithBankComplete.onComplete(0, "خطا در ارتباط با سرور");
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








  public interface onRentWithWalletComplete{
    void onComplete(int status, String message);
  }

  public interface onRentWithBankComplete{
    void onComplete(int status, String message);
  }



}
