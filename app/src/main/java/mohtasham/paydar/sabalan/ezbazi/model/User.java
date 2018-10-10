package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
  private int id;
  private String full_name;
  private String email;
  private String password;
  private String invite_code;
  private String client_key;
  private String token;






  public static class Parser implements Serializable{
    public static User parse(JSONObject object) throws JSONException {
      User user = new User();
      user.setId(object.getInt("id"));
      user.setFull_name(object.getString("full_name"));
      user.setEmail(object.getString("email"));
      user.setInvite_code(object.getString("invite_code"));
      return user;
    }
  }


  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public String getInvite_code() {
    return invite_code;
  }

  public void setInvite_code(String invite_code) {
    this.invite_code = invite_code;
  }

  public String getClient_key() {
    return client_key;
  }

  public void setClient_key(String client_key) {
    this.client_key = client_key;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
