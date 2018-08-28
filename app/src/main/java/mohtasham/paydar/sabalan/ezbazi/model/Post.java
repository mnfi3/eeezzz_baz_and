package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

  private int id;
  private String title;
  private String content;
  private String image_url;
  private String created_at;

  public static class Parser{
    public static Post parse(JSONObject postObj){
      Post post = new Post();
      try {
        post.setId(postObj.getInt("id"));
        post.setTitle(postObj.getString("title"));
        post.setContent(postObj.getString("content"));
        post.setCreated_at(postObj.getString("created_at"));
        post.setImage_url(postObj.getJSONArray("photos").getJSONObject(0).getString("url"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return post;
    }
  }



  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
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
