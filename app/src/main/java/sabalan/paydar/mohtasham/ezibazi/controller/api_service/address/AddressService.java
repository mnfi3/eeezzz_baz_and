package sabalan.paydar.mohtasham.ezibazi.controller.api_service.address;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sabalan.paydar.mohtasham.ezibazi.controller.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Address;
import sabalan.paydar.mohtasham.ezibazi.model.City;
import sabalan.paydar.mohtasham.ezibazi.model.State;

public class AddressService {
  private Context context;
  public AddressService(Context context){
    this.context = context;
  }




  public void getStates(final onStatesReceived onStatesReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.ADDRESS_STATES, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onStatesReceived.onComplete(status, message, null);
          return;
        }

        ArrayList<State> states = new ArrayList<State>();
        try {
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i< data.length() ; i++){
            State state;
            state = State.Parser.parse(data.getJSONObject(i));
            states.add(state);
          }

          onStatesReceived.onComplete(status, message, states);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }




  public void getStateCities(int state_id, final onStateCitiesReceived onStateCitiesReceived){
    String url = Urls.ADDRESS_STATE_CITIES;
    String id = Integer.toString(state_id);
    url = url.replace("{id}", id);

    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onStateCitiesReceived.onComplete(status, message, null);
          return;
        }

        ArrayList<City> cities = new ArrayList<City>();
        try {
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i< data.length() ; i++){
            City city;
            city = City.Parser.parse(data.getJSONObject(i));
            cities.add(city);
          }

          onStateCitiesReceived.onComplete(status, message, cities);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

  }



  public void saveAddress(JSONObject jsonObject, final onAddressSaved onAddressSaved){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.ADDRESS_INDEX, jsonObject, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onAddressSaved.onComplete(status, message, null);
          return;
        }

        Address address = new Address();
        try {
          address = Address.Parser.parse(response.getJSONObject("data"));
          onAddressSaved.onComplete(status, message, address);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }










  public interface onStatesReceived{
    void onComplete(int status, String message, ArrayList<State> states);
  }


  public interface onStateCitiesReceived{
    void onComplete(int status, String message, ArrayList<City> cities);
  }


  public interface onAddressSaved{
    void onComplete(int status, String message, Address address);
  }


}
