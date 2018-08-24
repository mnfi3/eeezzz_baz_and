package mohtasham.paydar.sabalan.ezbazi.model.common;


import org.json.JSONException;
import org.json.JSONObject;

public class Video {
  private int id;
  private String url;

  public static class Parser{
    public static Video parse(JSONObject jsonObject){
      Video video = new Video();
      try {
        video.setId(jsonObject.getInt("id"));
        video.setUrl(jsonObject.getString("url"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return video;
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


}
