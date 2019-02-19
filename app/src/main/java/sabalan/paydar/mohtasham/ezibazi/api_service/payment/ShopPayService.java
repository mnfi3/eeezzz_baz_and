package sabalan.paydar.mohtasham.ezibazi.api_service.payment;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;

public class ShopPayService {
  private Context context;
  public ShopPayService(Context context){
    this.context = context;
  }


  public void shopWithWallet(JSONObject jsonObject, final onShopWithWalletComplete onShopWithWalletComplete){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.SHOP_WITH_WALLET, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onShopWithWalletComplete.onComplete(status, message);
          return;
        }
        onShopWithWalletComplete.onComplete(status, message);
      }
    });
  }






  public void shopWithBank(JSONObject jsonObject, final onShopWithBankComplete onShopWithBankComplete){
    ApiRequest  apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.SHOP_WITH_BANK, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onShopWithBankComplete.onComplete(status, message, "");
          return;
        }

        String pay_url = "";
        try {
          pay_url = response.getString("data");
          onShopWithBankComplete.onComplete(status, message, pay_url);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }








  public interface onShopWithWalletComplete{
    void onComplete(int status, String message);
  }

  public interface onShopWithBankComplete{
    void onComplete(int status, String message, String pay_url);
  }



}
