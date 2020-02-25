package sabalan.paydar.mohtasham.ezibazi.api_service.account

import android.content.Context

import org.json.JSONException
import org.json.JSONObject


import sabalan.paydar.mohtasham.ezibazi.api_service.ApiRequest
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.model.Address
import sabalan.paydar.mohtasham.ezibazi.model.Finance
import sabalan.paydar.mohtasham.ezibazi.model.User

class UserDetailService(private val context: Context) {


    //  public void getUserDetail(JSONObject jsonObject, final onUserDetailReceivedComplete onUserDetailReceivedComplete){
    //    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Urls.LOGIN, jsonObject, new Response.Listener<JSONObject>() {
    //      @Override
    //      public void onResponse(JSONObject response) {
    //        int status = 0;
    //        String message = "";
    //        String token = "";
    //        User user = new User();
    //        try {
    //          status = response.getInt("status");
    //          message = response.getString("message");
    //          token = response.getJSONObject("data").getString("token");
    //          JSONObject userObj = response.getJSONObject("data").getJSONObject("user");
    //          user = User.Parser.parse(userObj);
    //          user.setToken(token);
    //          onUserDetailReceivedComplete.onComplete(status, message, user);
    //
    //        } catch (JSONException e) {
    //          e.printStackTrace();
    //        }
    //
    //      }
    //    }, new Response.ErrorListener() {
    //      @Override
    //      public void onErrorResponse(VolleyError error) {
    //        onUserDetailReceivedComplete.onComplete(0, "", new User());
    //      }
    //    });
    //
    //    request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //    Volley.newRequestQueue(context).add(request);
    //  }


    fun getUserFinance(onFinanceReceivedComplete: onFinanceReceivedComplete) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.USER_FINANCE, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onFinanceReceivedComplete.onComplete(status, message, Finance())
                    return
                }

                try {
                    val finance = Finance.Parser.parse(response!!.getJSONObject("data"))
                    onFinanceReceivedComplete.onComplete(status, message, finance)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    fun getUserLastAddress(onLastAddressReceived: onLastAddressReceived) {
        val apiRequest = ApiRequest(context)
        apiRequest.request(ApiRequest.GET, Urls.USER_LAST_ADDRESS, JSONObject(), true, object : ApiRequest.onDataReceived {
            override fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean) {
                if (error) {
                    onLastAddressReceived.onComplete(status, message, Address())
                    return
                }

                var address = Address()
                if (status == 1) {
                    try {
                        address = Address.Parser.parse(response!!.getJSONObject("data"))
                        onLastAddressReceived.onComplete(status, message, address)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

            }
        })


    }


    interface onUserDetailReceivedComplete {
        fun onComplete(status: Int, message: String, user: User)
    }


    interface onFinanceReceivedComplete {
        fun onComplete(status: Int, message: String, finance: Finance)
    }

    interface onLastAddressReceived {
        fun onComplete(status: Int, message: String, address: Address?)
    }


}
