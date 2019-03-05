package sabalan.paydar.mohtasham.ezibazi.api_service.comment;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest;
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls;
import sabalan.paydar.mohtasham.ezibazi.model.Comment;
import sabalan.paydar.mohtasham.ezibazi.model.Paginate;

public class CommentService {
  private Context context;
  public CommentService(Context context){
    this.context = context;
  }


  public void getComments(final int game_info_id, final int page_num, final onCommentsReceived onCommentsReceived){
    String url = Urls.GAME_INFO_COMMENTS +"/"+Integer.toString(game_info_id) + "?page=" + Integer.toString(page_num);

    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.GET, url, null, false, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onCommentsReceived.onReceived(status, message, new ArrayList<Comment>(), new Paginate());
          return;
        }

        List<Comment> comments = new ArrayList<>();
        Paginate paginate = null;
        try {
          JSONObject jsonData = response.getJSONObject("data");
          paginate = Paginate.Parser.parse(jsonData);
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
    });

  }

  public void addComment(JSONObject object, final onAddCommentReceived onAddCommentReceived){
    ApiRequest apiRequest = new ApiRequest(context);
    apiRequest.request(ApiRequest.POST, Urls.COMMENT_INDEX, object, true, new ApiRequest.onDataReceived() {
      @Override
      public void onReceived(JSONObject response, int status, String message, boolean error) {
        if (error){
          onAddCommentReceived.onReceived(status, message, new Comment());
          return;
        }

        Comment comment;
        try {
          comment = Comment.Parser.parse(response.getJSONObject("data"));
          onAddCommentReceived.onReceived(status, message, comment);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });



  }





  public interface onCommentsReceived{
    void onReceived(int status, String message, List<Comment> comments, Paginate paginate);
  }

  public interface onAddCommentReceived{
    void onReceived(int status, String message, Comment comment);
  }


}
