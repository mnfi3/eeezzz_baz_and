package mohtasham.paydar.sabalan.ezbazi.model.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

  private int id;
  private String url;
  private int width;
  private int height;

  public static class Parser{
    public static Photo parse(JSONObject jsonObject){
      Photo photo = new Photo();
      try {
        photo.setId(jsonObject.getInt("id"));
        photo.setUrl(jsonObject.getString("url"));
        photo.setWidth(jsonObject.getInt("width"));
        photo.setHeight(jsonObject.getInt("height"));

      } catch (JSONException e) {
        e.printStackTrace();
      }
      return photo;
    }
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
