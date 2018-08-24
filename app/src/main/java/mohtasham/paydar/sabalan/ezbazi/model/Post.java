package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

  private int id;
  private String title;
  private String content;
  private String image_url;

  public static class Parser{
    public static Post parse(JSONObject postObj){
      Post post = new Post();
      try {
        post.setId(postObj.getInt("id"));
        post.setTitle(postObj.getString("title"));
        post.setContent(postObj.getString("content"));
        post.setImage_url(postObj.getJSONArray("photos").getJSONObject(0).getString("url"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return post;
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

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }
}