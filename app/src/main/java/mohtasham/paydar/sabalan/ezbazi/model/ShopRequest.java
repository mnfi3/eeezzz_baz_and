package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopRequest {
  private int id;
  private int user_id;
  private int game_for_shop_id;
  private int address_id;
  private int game_price;
  private int is_sent;
  private int is_success;
  private int is_finish;
  private String created_at;
  private Game game;


  public static class Parser{
    public static ShopRequest parse(JSONObject object){
      ShopRequest request = new ShopRequest();
      try {
        request.setId(object.getInt("id"));
        request.setUser_id(object.getInt("user_id"));
        request.setGame_for_shop_id(object.getInt("game_for_shop_id"));
        request.setAddress_id(object.getInt("address_id"));
        request.setGame_price(object.getInt("game_price"));
        request.setIs_sent(object.getInt("is_sent"));
        request.setIs_success(object.getInt("is_success"));
        request.setIs_finish(object.getInt("is_finish"));
        request.setCreated_at(object.getString("created_at"));
        request.setGame(Game.Parser.parse(object.getJSONObject("game_for_shop")));


      } catch (JSONException e) {
        e.printStackTrace();
      }
      return request;
    }
  }

  public int getIs_sent() {
    return is_sent;
  }

  public void setIs_sent(int is_sent) {
    this.is_sent = is_sent;
  }

  public int getGame_price() {
    return game_price;
  }

  public void setGame_price(int game_price) {
    this.game_price = game_price;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getGame_for_shop_id() {
    return game_for_shop_id;
  }

  public void setGame_for_shop_id(int game_for_shop_id) {
    this.game_for_shop_id = game_for_shop_id;
  }

  public int getAddress_id() {
    return address_id;
  }

  public void setAddress_id(int address_id) {
    this.address_id = address_id;
  }

  public int getIs_success() {
    return is_success;
  }

  public void setIs_success(int is_success) {
    this.is_success = is_success;
  }

  public int getIs_finish() {
    return is_finish;
  }

  public void setIs_finish(int is_finish) {
    this.is_finish = is_finish;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

}
