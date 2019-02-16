package sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;



import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;

public class FinanceService {
  private Context context;
  public FinanceService(Context context){
    this.context = context;
  }


  public void getPayUrl(JSONObject jsonObject, final onCreditComplete onCreditComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.INCREASE_CREDIT, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onCreditComplete.onComplete(status, message, "");
          return;
        }

        String url = "";
        try {
          url = response.getString("data");
          onCreditComplete.onComplete(status, message, url);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });

  }










  public interface onCreditComplete{
    void onComplete(int status, String message, String url);
  }



}
