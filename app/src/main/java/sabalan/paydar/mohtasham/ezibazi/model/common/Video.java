package sabalan.paydar.mohtasham.ezibazi.model.common;


import org.json.JSONException;
import org.json.JSONObject;

public class Video {
  private int id;
  private String url;
  private String type;

  public static class Parser{
    public static Video parse(JSONObject jsonObject){
      Video video = new Video();
      try {
        video.setId(jsonObject.getInt("id"));
        video.setUrl(jsonObject.getString("url"));
        video.setType(jsonObject.getString("type"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return video;
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


}
