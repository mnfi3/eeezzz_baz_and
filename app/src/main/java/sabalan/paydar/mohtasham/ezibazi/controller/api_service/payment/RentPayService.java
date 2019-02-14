package sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;

public class RentPayService {
  private Context context;
  public RentPayService(Context context){
    this.context = context;
  }


  public void rentWithWallet(JSONObject jsonObject, final onRentWithWalletComplete onRentWithWalletComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.RENT_WITH_WALLET, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRentWithWalletComplete.onComplete(status, message);
          return;
        }
        onRentWithWalletComplete.onComplete(status, message);
      }
    });

  }



  public void rentWithBank(JSONObject jsonObject, final onRentWithBankComplete onRentWithBankComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.RENT_WITH_BANK, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if(error){
          onRentWithBankComplete.onComplete(status, message, "");
          return;
        }
        String url = "";
        try {
          url = response.getString("data");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onRentWithBankComplete.onComplete(status, message, url);
      }
    });

  }








  public interface onRentWithWalletComplete{
    void onComplete(int status, String message);
  }

  public interface onRentWithBankComplete{
    void onComplete(int status, String message, String pay_url);
  }



}
