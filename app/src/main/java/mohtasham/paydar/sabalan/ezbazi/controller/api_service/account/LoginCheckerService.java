package mohtasham.paydar.sabalan.ezbazi.controller.api_service.account;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mohtasham.paydar.sabalan.ezbazi.controller.api_service.Urls;
import mohtasham.paydar.sabalan.ezbazi.model.User;

import static android.content.ContentValues.TAG;

public class LoginCheckerService {
  private Context context;
  public LoginCheckerService(Context context){
    this.context = context;
  }


  public void check(final User user, final onLoginCheckComplete onLoginCheckComplete){
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Urls.LOGIN_CHECK, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int status = 0;
        String user_name = "";
        boolean isLoggedIn = false;
        try {
          status = response.getInt("status");
          if (status == 1){
            isLoggedIn = true;
            user_name = response.getJSONObject("data").getString("user_name");
            Log.i(TAG, "onResponse: user_name = " + user_name);
          }else {
            isLoggedIn = false;
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }

        onLoginCheckComplete.onComplete(isLoggedIn, user_name);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onLoginCheckComplete.onComplete(false, "");
      }
    }){
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Accept", "application/json");
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImI2OGExNDBhOTIxYTM1NmE0NWQ2MzZjMDRkMmE5YmEzZGE5MGVkMTczNzhlZWVkM2Q0NmNlZjYyOTVmNmQxNjUyMDNlNmNhOGZkNmM4N2ZlIn0.eyJhdWQiOiIzIiwianRpIjoiYjY4YTE0MGE5MjFhMzU2YTQ1ZDYzNmMwNGQyYTliYTNkYTkwZWQxNzM3OGVlZWQzZDQ2Y2VmNjI5NWY2ZDE2NTIwM2U2Y2E4ZmQ2Yzg3ZmUiLCJpYXQiOjE1MzU3OTM2MzIsIm5iZiI6MTUzNTc5MzYzMiwiZXhwIjoxNTY3MzI5NjMxLCJzdWIiOiIzMyIsInNjb3BlcyI6W119.VxVsR-7pc9Zu_9J0gOn_z4ooY2H_GrzMoA3GU2hMD6z1a7pM3AFx0jOMlVSGcghnf11pVGcWjqwqJuVJWNpXE4gTrrHbp8669mQYHKoOTP6yCUpf2etu88amTOdrtJ8E6P45pCCr4Ln_L2lehwKJ9kK2-G4J29eSb_NYuZxBG2IwlTsr_dX9vIIU426a4QgZk6J8eJ_7mDRoVJeFMiyyvKotvAOef5pZsAi-h6cG5RI-bM6SAZ2zW1n5lM78uTIgdvQdz2Bwi5JYbzTqwOBS9WArqwJuDMhmSYNyy5AKszkeRaI-dVmMPnUo3r0sXbz8F14w2xYZs6jXxso7RjKIRj6-1uF2k2K684wDW0FeinDuafOxlfPnZx-ojCOFTVkGh3t3q1eCaueVqbzXLT7RGbqjbFjx3m7876erbavPyRz6_oU97rBP0QSNEl-DxMlVsOxpMvB2BVM-rSIoo6Pg8aOUC1tS58OAxJWMc-iWnub-p98AdtObK5z0EPx2jlPVgWYtMcqUjjQvGINt11BA-vLtzb55J9t0_Rj2X2xS83msf5fsCFYdOnHO5-FR1e7Fi56ygDqc64d6UktKT6v5KNVSjEdSzUrpUAETCdndfvMIc0g98qFt6LebncbFIYr1ouGf5tY3T9OyNePyWEASFFFkAAKhdp_Ai2qjf-0-1g8";
        params.put("Authorization", "Bearer " + user.getToken());
        return params;
      }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public interface onLoginCheckComplete{
    void onComplete(boolean isLoggedIn, String user_name);
  }


}
