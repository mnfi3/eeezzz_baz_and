package mohtasham.paydar.sabalan.ezbazi.controller.api_service.comment;

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
import mohtasham.paydar.sabalan.ezbazi.model.Comment;
import mohtasham.paydar.sabalan.ezbazi.model.MainSlider;
import mohtasham.paydar.sabalan.ezbazi.model.Paginate;

public class CommentService {
  private Context context;
  public CommentService(Context context){
    this.context = context;
  }


  public void getComments(final int game_info_id, final int page_num, final onCommentsReceived onCommentsReceived){
    String url = Urls.GAME_INFO_COMMENTS +"/"+Integer.toString(game_info_id) + "?page=" + page_num;
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<MainSlider> sliders = new ArrayList<>();
        int status = 0;
        String message = "";
        try {
          status = response.getInt("status");
          message = response.getString("message");
          List<Comment> comments =  new ArrayList<Comment>();
          JSONObject jsonData = response.getJSONObject("data");
          Paginate paginate = Paginate.Parser.parse(jsonData);
          JSONArray data = jsonData.getJSONArray("data");
          for (int i=0 ; i<data.length() ; i++){
            JSONObject commentObj = data.getJSONObject(i);
            comments.add( Comment.Parser.parse(commentObj));
          }


          onCommentsReceived.onReceived(status, message, comments, paginate);

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onCommentsReceived.onReceived(0, "", new ArrayList<Comment>(), new Paginate());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public interface onCommentsReceived{
    void onReceived(int status, String message, List<Comment> comments, Paginate paginate);
  }


}