package sabalan.paydar.mohtasham.ezibazi.api_service.main;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.CityPrefManager;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;

public class RentService {
  private Context context;
  public RentService(Context context){
    this.context = context;
  }


  public void getRents(final int page_num, final onRentsReceived onRentsReceived){
    String url = Urls.RENT_INDEX0 + "/"+ Integer.toString(new CityPrefManager(context).getCityId()) + "?page=" + page_num;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRentsReceived.onReceived(status, message, new ArrayList<Game>(), new Paginate());
          return;
        }

        try {
          List<Game> games =  new ArrayList<>();
          JSONObject jsonData = response.getJSONObject("data");
          Paginate paginate = Paginate.Parser.parse(jsonData);
          JSONArray data = jsonData.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject gameObj = data.getJSONObject(i);
            games.add( Game.Parser.parse(gameObj));
          }

          onRentsReceived.onReceived(status, message, games, paginate);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });



  }



  public void getSearchedRents(JSONObject obj, final onSearchedRentsReceived onSearchedRentsReceived){
    String url = Urls.RENT_SEARCH;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.RENT_SEARCH, obj, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSearchedRentsReceived.onReceived(status, message, new ArrayList<Game>());
          return;
        }

        try {
          List<Game> games =  new ArrayList<>();
          if(status == 1) {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
              games.add(Game.Parser.parse(data.getJSONObject(i)));
            }
          }

          onSearchedRentsReceived.onReceived(status, message, games);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

  }



  public void getRelatedRents(final int game_id, final onRelatedRentsReceived onRelatedRentsReceived){
    String url = Urls.GAME_RELATED_GAME_FOR_RENT + "/" + Integer.toString(game_id) ;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRelatedRentsReceived.onReceived(status, message, new ArrayList<Game>());
          return;
        }

        try {
          List<Game> games =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for(int i=0 ; i<data.length();i++){
            games.add(Game.Parser.parse(data.getJSONObject(i)));
          }

          onRelatedRentsReceived.onReceived(status, message, games);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }


  public void getSpecialRent(int id, final onSpecialRentReceived onSpecialRentReceived){
    String url = Urls.RENT_INDEX + "/" + Integer.toString(id);
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSpecialRentReceived.onReceived(status, message, new Game());
          return;
        }

        Game game;
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONObject gameObj = response.getJSONObject("data");
          game = Game.Parser.parse(gameObj);

          onSpecialRentReceived.onReceived(status, message, game);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }




  public void getRentTypes(final onRentTypesReceived onRentTypesReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, Urls.RENT_TYPE_INDEX, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRentTypesReceived.onReceived(status, message, new ArrayList<RentType>());
          return;
        }

        List<RentType> rentTypes = new ArrayList<>();
        try {
          status = response.getInt("status");
          message = response.getString("message");
          JSONArray data = response.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject rentTypeObj = data.getJSONObject(i);
            rentTypes.add( RentType.Parser.parse(rentTypeObj));
          }

          onRentTypesReceived.onReceived(status, message, rentTypes);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

  }




  public interface onRentsReceived{
    void onReceived(int status, String message, List<Game> games, Paginate paginate);
  }

  public interface onSearchedRentsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onRelatedRentsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onSpecialRentReceived{
    void onReceived(int status, String message, Game game);
  }

  public interface onRentTypesReceived{
    void onReceived(int status, String message, List<RentType> rentTypes);
  }

}
