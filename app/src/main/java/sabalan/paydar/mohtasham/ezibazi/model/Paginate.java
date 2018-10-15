package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Paginate implements Serializable {

  private int current_page;
  private String first_page_url;
  private int from;
  private int last_page;
  private String last_page_url;
  private String next_page_url;
  private String prev_page_url;
  private String path;
  private int per_page;
  private int to;
  private int total;

  public static class Parser  implements Serializable{
    public static Paginate parse(JSONObject response){
      Paginate paginate = new Paginate();
      try {
        paginate.setCurrent_page(response.getInt("current_page"));
        paginate.setFirst_page_url(response.getString("first_page_url"));
        paginate.setFrom(response.getInt("from"));
        paginate.setLast_page(response.getInt("last_page"));
        paginate.setLast_page_url(response.getString("last_page_url"));
        paginate.setNext_page_url(response.getString("next_page_url"));
        paginate.setPath(response.getString("path"));
        paginate.setPer_page(response.getInt("per_page"));
        paginate.setPrev_page_url(response.getString("prev_page_url"));
        paginate.setTo(response.getInt("to"));
        paginate.setTotal(response.getInt("total"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return paginate;
    }
  }


  public void setCurrent_page(int current_page){
    this.current_page = current_page;
  }

  public int getCurrent_page() {
    return current_page;
  }

  public int getFrom() {
    return from;
  }

  public int getLast_page() {
    return last_page;
  }

  public int getPer_page() {
    return per_page;
  }

  public int getTo() {
    return to;
  }

  public String getFirst_page_url() {
    return first_page_url;
  }

  public int getTotal() {
    return total;
  }

  public String getLast_page_url() {
    return last_page_url;
  }

  public String getNext_page_url() {
    return next_page_url;
  }

  public String getPath() {
    return path;
  }

  public String getPrev_page_url() {
    return prev_page_url;
  }

  public void setFirst_page_url(String first_page_url) {
    this.first_page_url = first_page_url;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public void setLast_page(int last_page) {
    this.last_page = last_page;
  }

  public void setLast_page_url(String last_page_url) {
    this.last_page_url = last_page_url;
  }

  public void setNext_page_url(String next_page_url) {
    this.next_page_url = next_page_url;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setPer_page(int per_page) {
    this.per_page = per_page;
  }

  public void setPrev_page_url(String prev_page_url) {
    this.prev_page_url = prev_page_url;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public void setTotal(int total) {
    this.total = total;
  }

}
