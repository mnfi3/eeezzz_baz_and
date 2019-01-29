package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

  private int id;
  private String url;
  private String type;
  private int width;
  private int height;

  public static class Parser{
    public static Photo parse(JSONObject jsonObject){
      Photo photo = new Photo();
      try {
        photo.setId(jsonObject.getInt("id"));
        photo.setUrl(jsonObject.getString("url"));
        photo.setType(jsonObject.getString("type"));
        photo.setWidth(jsonObject.getInt("width"));
        photo.setHeight(jsonObject.getInt("height"));

      } catch (JSONException e) {
        e.printStackTrace();
      }
      return photo;
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }


}
