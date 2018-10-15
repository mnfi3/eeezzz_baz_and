package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ticket {
  private int id;
  private int is_user_sent;
  private String text;
  private int is_seen;
  private String created_at;

  public static class Parser{
    public static Ticket parse(JSONObject object) throws JSONException {
      Ticket ticket = new Ticket();
      ticket.setId(object.getInt("id"));
      ticket.setIs_user_sent(object.getInt("is_user_sent"));
      ticket.setText(object.getString("text"));
      ticket.setIs_seen(object.getInt("is_seen"));
      ticket.setCreated_at(object.getString("created_at"));
      return ticket;
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIs_user_sent() {
    return is_user_sent;
  }

  public void setIs_user_sent(int is_user_sent) {
    this.is_user_sent = is_user_sent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getIs_seen() {
    return is_seen;
  }

  public void setIs_seen(int is_seen) {
    this.is_seen = is_seen;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }


}
