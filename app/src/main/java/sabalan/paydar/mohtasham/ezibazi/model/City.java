package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class City {
  private int id;
  private int state_id;
  private String name;

  public static class Parser{

    public static City parse(JSONObject object){
      City city = new City();
      try {
        city.setId(object.getInt("id"));
        city.setState_id(object.getInt("state_id"));
        city.setName(object.getString("name"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return city;
    }
  }



  public int getState_id() {
    return state_id;
  }

  public void setState_id(int state_id) {
    this.state_id = state_id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
