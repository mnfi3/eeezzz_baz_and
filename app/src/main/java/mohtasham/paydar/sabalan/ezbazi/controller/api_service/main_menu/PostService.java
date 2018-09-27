package mohtasham.paydar.sabalan.ezbazi.controller.api_service.main_menu;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.Urls;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;
import mohtasham.paydar.sabalan.ezbazi.model.Post;

public class PostService {
  private Context context;
  public PostService(Context context){
    this.context = context;
  }


  public void getMainPosts(final int page_num, final onPostsReceived onPostsReceived){
    String url = Urls.POST_INDEX + "?page=" + page_num;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<MainSlider> sliders = new ArrayList<>();
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
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
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onPostsReceived.onReceived(0, "", new ArrayList<Post>(), new Paginate());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }

  public void getSearchedPosts(JSONObject object, final onSearchedPostsReceived onSearchedPostsReceived){
    String url = Urls.POST_SEARCH;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
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
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSearchedPostsReceived.onReceived(0, "", new ArrayList<Post>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getRelatedPosts(final int game_id, final onRelatedPostsReceived onRelatedPostsReceived){
    String url = Urls.GAME_RELATED_POSTS + "/" + Integer.toString(game_id);
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<MainSlider> sliders = new ArrayList<>();
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
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
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRelatedPostsReceived.onReceived(0, "خطا در ارتیاط با سرور", new ArrayList<Post>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
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
