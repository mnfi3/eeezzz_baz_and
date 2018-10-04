package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RentType {
  private int id;
  private int day_count;
  private int price_percent;

  public static class Parser{
    public static RentType parse(JSONObject object){
      RentType rentType = new RentType();
      try {
        rentType.setId(object.getInt("id"));
        rentType.setDay_count(object.getInt("day_count"));
        rentType.setPrice_percent(object.getInt("price_percent"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return rentType;
    }

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getDay_count() {
    return day_count;
  }

  public void setDay_count(int day_count) {
    this.day_count = day_count;
  }

  public int getPrice_percent() {
    return price_percent;
  }

  public void setPrice_percent(int price_percent) {
    this.price_percent = price_percent;
  }
}
