package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Device {
  private int id;
  private int user_id;
  private String device_type;
  private String client_key;
  private String fcm_token;
  private String created_at;


  public static class Parser{
    public static Device parse(JSONObject object){
      Device device = new Device();
      try {
        device.setId(object.getInt("id"));
        device.setUser_id(object.getInt("user_id"));
        device.setDevice_type(object.getString("device_type"));
        device.setFcm_token(object.getString("client_key"));
        device.setDevice_type(object.getString("fcm_token"));
        device.setCreated_at(object.getString("created_at"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return device;
    }
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

  public String getDevice_type() {
    return device_type;
  }

  public void setDevice_type(String device_type) {
    this.device_type = device_type;
  }

  public String getClient_key() {
    return client_key;
  }

  public void setClient_key(String client_key) {
    this.client_key = client_key;
  }

  public String getFcm_token() {
    return fcm_token;
  }

  public void setFcm_token(String fcm_token) {
    this.fcm_token = fcm_token;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }
}
