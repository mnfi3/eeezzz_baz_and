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

public class ShopPayService {
  private Context context;
  public ShopPayService(Context context){
    this.context = context;
  }


  public void shopWithWallet(JSONObject jsonObject, final onShopWithWalletComplete onShopWithWalletComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.SHOP_WITH_WALLET, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          onShopWithWalletComplete.onComplete(status, message);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onShopWithWalletComplete.onComplete(0, "خطا در ارتباط با سرور");
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






  public void shopWithBank(JSONObject jsonObject, final onShopWithBankComplete onShopWithBankComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.SHOP_WITH_BANK, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        String pay_url = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          pay_url = response.getString("data");
          onShopWithBankComplete.onComplete(status, message, pay_url);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onShopWithBankComplete.onComplete(0, "خطا در ارتباط با سرور", "");
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








  public interface onShopWithWalletComplete{
    void onComplete(int status, String message);
  }

  public interface onShopWithBankComplete{
    void onComplete(int status, String message, String pay_url);
  }



}
