package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
  private int id;
  private int user_id;
  private int commentable_id;
  private String commentable_type;
  private String text;
  private String created_at;
  private User user;

  public static class Parser{
    public static Comment parse(JSONObject jsonObject){
      Comment comment = new Comment();
      try {
        JSONObject userObj = jsonObject.getJSONObject("user");
        comment.setUser(User.Parser.parse(userObj));
        comment.setId(jsonObject.getInt("id"));
        comment.setUser_id(jsonObject.getInt("user_id"));
        comment.setCommentable_id(jsonObject.getInt("commentable_id"));
        comment.setCommentable_type(jsonObject.getString("commentable_type"));
        comment.setText(jsonObject.getString("text"));
        comment.setCreated_at(jsonObject.getString("created_at"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return comment;
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getCommentable_id() {
    return commentable_id;
  }

  public void setCommentable_id(int commentable_id) {
    this.commentable_id = commentable_id;
  }

  public String getCommentable_type() {
    return commentable_type;
  }

  public void setCommentable_type(String commentable_type) {
    this.commentable_type = commentable_type;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
