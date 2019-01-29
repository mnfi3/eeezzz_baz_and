package sabalan.paydar.mohtasham.ezibazi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Post  implements Serializable {

  private int id;
  private String title;
  private String content;
  private String image_url;
  private String created_at;

  public static class Parser implements Serializable{
    public static Post parse(JSONObject postObj){
      Post post = new Post();
      try {
        post.setId(postObj.getInt("id"));
        post.setTitle(postObj.getString("title"));
        post.setContent(postObj.getString("content"));
        post.setCreated_at(postObj.getString("created_at"));
        JSONArray photoArray = postObj.getJSONArray("photos");
        for (int i=0 ; i<photoArray.length() ; i++){
          Photo photo;
          photo = Photo.Parser.parse(photoArray.getJSONObject(i));
          if(photo.getType().equals("app_cover")){
            post.setImage_url(photo.getUrl());
            break;
          }
        }

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
