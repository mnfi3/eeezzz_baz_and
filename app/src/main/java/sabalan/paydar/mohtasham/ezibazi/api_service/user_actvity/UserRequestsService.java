package sabalan.paydar.mohtasham.ezibazi.api_service.user_actvity;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.RentRequest;
import sabalan.paydar.mohtasham.ezibazi.model.ShopRequest;

public class UserRequestsService {
  private Context context;
  public UserRequestsService(Context context){
    this.context = context;
  }


  public void getRentRequests( final onRentRequestsReceived onRentRequestsReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.USER_RENT_REQUESTS, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRentRequestsReceived.onReceived(status, message, new ArrayList<RentRequest>());
          return;
        }

        try {
          List<RentRequest> requests =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject requestObj = data.getJSONObject(i);
            requests.add(RentRequest.Parser.parse(requestObj));
          }

          onRentRequestsReceived.onReceived(status, message, requests);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }



  public void getShopRequests( final onShopRequestsReceived onShopRequestsReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.USER_BUY_REQUESTS, null, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onShopRequestsReceived.onReceived(status, message, new ArrayList<ShopRequest>());
          return;
        }

        try {
          List<ShopRequest> requests =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject requestObj = data.getJSONObject(i);
            requests.add(ShopRequest.Parser.parse(requestObj));
          }

          onShopRequestsReceived.onReceived(status, message, requests);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }










  public interface onRentRequestsReceived{
    void onReceived(int status, String message, List<RentRequest> requests);
  }

  public interface onShopRequestsReceived{
    void onReceived(int status, String message, List<ShopRequest> requests);
  }



}
