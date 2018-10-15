package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MainSlider implements Serializable {
  private int id;
  private String title;
  private String content;
  private String on_click;
  private String image_url;


  public static class Parser  implements Serializable{
    public static MainSlider parse(JSONObject sliderObject) throws JSONException {
      MainSlider slider = new MainSlider();
      slider.setId(sliderObject.getInt("id"));
      slider.setTitle(sliderObject.getString("title"));
      slider.setContent(sliderObject.getString("content"));
      slider.setOn_click(sliderObject.getString("on_click"));
      JSONArray photo = sliderObject.getJSONArray("photos");
      slider.setImage_url(photo.getJSONObject(0).getString("url"));

      return slider;
    }
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getOn_click() {
    return on_click;
  }

  public void setOn_click(String on_click) {
    this.on_click = on_click;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }
}
