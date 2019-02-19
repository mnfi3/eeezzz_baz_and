package sabalan.paydar.mohtasham.ezibazi.api_service.main;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;
import sabalan.paydar.mohtasham.ezibazi.model.Post;

public class PostService {
  private Context context;
  public PostService(Context context){
    this.context = context;
  }


  public void getMainPosts(final int page_num, final onPostsReceived onPostsReceived){
    String url = Urls.POST_INDEX + "?page=" + page_num;
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onPostsReceived.onReceived(status, message, new ArrayList<Post>(), new Paginate());
          return;
        }

        try {
          List<Post> posts =  new ArrayList<Post>();
          JSONObject jsonData = response.getJSONObject("data");
          Paginate paginate = Paginate.Parser.parse(jsonData);
          JSONArray data = jsonData.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject postObj = data.getJSONObject(i);
            posts.add( Post.Parser.parse(postObj));
          }

          onPostsReceived.onReceived(status, message, posts, paginate);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });


  }

  public void getSearchedPosts(JSONObject object, final onSearchedPostsReceived onSearchedPostsReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.POST_SEARCH, object, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onSearchedPostsReceived.onReceived(status, message, new ArrayList<Post>());
          return;
        }

        try {
          List<Post> posts =  new ArrayList<Post>();
          if(status == 1) {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
              posts.add(Post.Parser.parse(data.getJSONObject(i)));
            }
          }

          onSearchedPostsReceived.onReceived(status, message, posts);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }


  public void getRelatedPosts(final int game_id, final onRelatedPostsReceived onRelatedPostsReceived){
    String url = Urls.GAME_RELATED_POSTS + "/" + Integer.toString(game_id);
    ApiRequest  apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onRelatedPostsReceived.onReceived(status, message, new ArrayList<Post>());
          return;
        }

        try {
          List<Post> posts =  new ArrayList<Post>();
          JSONArray data = response.getJSONArray("data");
          for (int i=0;i<data.length();i++){
            posts.add(Post.Parser.parse(data.getJSONObject(i)));
          }

          onRelatedPostsReceived.onReceived(status, message, posts);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });


  }




  public interface onPostsReceived{
    void onReceived(int status, String message, List<Post> posts, Paginate paginate);
  }

  public interface onSearchedPostsReceived{
    void onReceived(int status, String message, List<Post> posts);
  }


  public interface onRelatedPostsReceived{
    void onReceived(int status, String message, List<Post> posts);
  }



}
