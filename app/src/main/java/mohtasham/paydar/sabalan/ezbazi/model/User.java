package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
  private int id;
  private String first_name;
  private String last_name;
  private String user_name;
  private String mobile_number;
  private String email;
  private String password;
  private String invite_code;

  private String client_key;
  private String token;






  public static class Parser implements Serializable{
    public static User parse(JSONObject object) throws JSONException {
      User user = new User();
      user.setId(object.getInt("id"));
      user.setFirst_name(object.getString("first_name"));
      user.setLast_name(object.getString("last_name"));
      user.setUser_name(object.getString("user_name"));
      user.setMobile_number(object.getString("mobile_number"));
      user.setEmail(object.getString("email"));
      user.setInvite_code(object.getString("invite_code"));
      return user;
    }
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

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getMobile_number() {
    return mobile_number;
  }

  public void setMobile_number(String mobile_number) {
    this.mobile_number = mobile_number;
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
