package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class State {
  private int id;
  private String name;

  public static class Parser{

    public static State parse(JSONObject object){
      State state = new State();
      try {
        state.setId(object.getInt("id"));
        state.setName(object.getString("name"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return state;
    }
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
