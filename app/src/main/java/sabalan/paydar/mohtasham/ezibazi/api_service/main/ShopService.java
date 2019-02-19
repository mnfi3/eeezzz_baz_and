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

public class ShopService {
  private Context context;
  public ShopService(Context context){
    this.context = context;
  }


  public void getMainShops(final int page_num, final onShopsReceived onShopsReceived){
    String url = Urls.SHOP_INDEX0 + "/"+ Integer.toString(new CityPrefManager(context).getCityId()) + "?page=" + page_num;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onShopsReceived.onReceived(status, message, new ArrayList<Game>(), new Paginate());
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

          onShopsReceived.onReceived(status, message, games, paginate);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }


  public void getSearchedShops(JSONObject obj, final onSearchedShopsReceived onSearchedShopsReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.SHOP_SEARCH, obj, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSearchedShopsReceived.onReceived(status, message, new ArrayList<Game>());
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

          onSearchedShopsReceived.onReceived(status, message, games);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }


  public void getRelatedShops(final int game_id, final onRelatedShopsReceived onRelatedShopsReceived){
    String url = Urls.GAME_RELATED_GAME_FOR_SHOP + "/"+Integer.toString(game_id) ;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRelatedShopsReceived.onReceived(status, message, new ArrayList<Game>());
          return;
        }

        try {
          List<Game> games =  new ArrayList<>();
          JSONArray data = response.getJSONArray("data");
          for(int i=0 ; i<data.length();i++){
            games.add(Game.Parser.parse(data.getJSONObject(i)));
          }

          onRelatedShopsReceived.onReceived(status, message, games);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

  }


  public void getSpecialShop(int id, final onSpecialShopReceived onSpecialshopReceived){
    String url = Urls.SHOP_INDEX + "/" + Integer.toString(id);
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSpecialshopReceived.onReceived(status, message, new Game());
          return;
        }

        Game game = new Game();
        try {
          JSONObject gameObj = response.getJSONObject("data");
          game = Game.Parser.parse(gameObj);

          onSpecialshopReceived.onReceived(status, message, game);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }


  public interface onShopsReceived{
    void onReceived(int status, String message, List<Game> games, Paginate paginate);
  }

  public interface onSearchedShopsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onRelatedShopsReceived{
    void onReceived(int status, String message, List<Game> games);
  }

  public interface onSpecialShopReceived{
    void onReceived(int status, String message, Game game);
  }


}
