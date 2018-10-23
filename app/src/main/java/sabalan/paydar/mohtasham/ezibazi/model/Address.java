package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {
  private int id;
  private int user_id;
  private int state_id;
  private int city_id;
  private String postcode;
  private String home_phone_number;
  private String content;
  private double latitude;
  private double longitude;
  private String created_at;
  private State state;
  private City city;


  public static class Parser{
    public static Address parse(JSONObject object){
      Address address = new Address();
      try {
        address.setId(object.getInt("id"));
        address.setUser_id(object.getInt("user_id"));
        address.setState_id(object.getInt("state_id"));
        address.setCity_id(object.getInt("city_id"));
        address.setPostcode(object.getString("postcode"));
        address.setHome_phone_number(object.getString("home_phone_number"));
        address.setContent(object.getString("content"));
        address.setLatitude(object.getDouble("latitude"));
        address.setLatitude(object.getDouble("longitude"));
        address.setCreated_at(object.getString("created_at"));
        address.setState(State.Parser.parse(object.getJSONObject("state")));
        address.setCity(City.Parser.parse(object.getJSONObject("city")));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return address;
    }
  }


  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
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

  public int getState_id() {
    return state_id;
  }

  public void setState_id(int state_id) {
    this.state_id = state_id;
  }

  public int getCity_id() {
    return city_id;
  }

  public void setCity_id(int city_id) {
    this.city_id = city_id;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getHome_phone_number() {
    return home_phone_number;
  }

  public void setHome_phone_number(String home_phone_number) {
    this.home_phone_number = home_phone_number;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }
}
