package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RentRequest {
  private int id;
  private int user_id;
  private int game_for_rent_id;
  private int address_id;
  private int game_price;
  private int rent_price;
  private int is_sent;
  private int is_success;
  private int is_finish;
  private String finished_at;
  private String created_at;
  private int rent_day_count;
  private Game game;


  public static class Parser{
    public static RentRequest parse(JSONObject object){
      RentRequest request = new RentRequest();
      try {
        request.setId(object.getInt("id"));
        request.setUser_id(object.getInt("user_id"));
        request.setGame_for_rent_id(object.getInt("game_for_rent_id"));
        request.setAddress_id(object.getInt("address_id"));
        request.setGame_price(object.getInt("game_price"));
        request.setRent_price(object.getInt("rent_price"));
        request.setIs_sent(object.getInt("is_sent"));
        request.setIs_success(object.getInt("is_success"));
        request.setIs_finish(object.getInt("is_finish"));
        request.setFinished_at(object.getString("finished_at"));
        request.setCreated_at(object.getString("created_at"));
        request.setRent_day_count(object.getJSONObject("rent_type").getInt("day_count"));
        request.setGame(Game.Parser.parse(object.getJSONObject("game_for_rent")));


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

  public int getGame_for_rent_id() {
    return game_for_rent_id;
  }

  public void setGame_for_rent_id(int game_for_rent_id) {
    this.game_for_rent_id = game_for_rent_id;
  }

  public int getAddress_id() {
    return address_id;
  }

  public void setAddress_id(int address_id) {
    this.address_id = address_id;
  }

  public int getRent_price() {
    return rent_price;
  }

  public void setRent_price(int rent_price) {
    this.rent_price = rent_price;
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

  public String getFinished_at() {
    return finished_at;
  }

  public void setFinished_at(String finished_at) {
    this.finished_at = finished_at;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public int getRent_day_count() {
    return rent_day_count;
  }

  public void setRent_day_count(int rent_day_count) {
    this.rent_day_count = rent_day_count;
  }


  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

}
