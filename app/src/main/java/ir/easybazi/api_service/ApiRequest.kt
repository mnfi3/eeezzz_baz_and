package ir.easybazi.api_service

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import ir.easybazi.system.auth.Auth
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener


class ApiRequest(private val context: Context) {

    companion object {
        private val TAG = "ApiRequest"
        val GET = 0
        val POST = 1
        val PUT = 2
        val DELETE = 3
        val HEAD = 4
        val OPTION = 5
        val TRACE = 6
        val PATCH = 7
    }


    //json object request
    fun request(method: Int, url: String, `object`: JSONObject, auth: Boolean, listener: onDataReceived) {


        AndroidNetworking.initialize(context)
        if(method == GET) {
            AndroidNetworking.get(url)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer " + Auth.userToken)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            var status = 0
                            var message = ""
                            try {
                                status = response.getInt("status")
                                message = response.getString("message")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            listener.onReceived(response, status, message, false)
                        }

                        override fun onError(anError: ANError?) {
                            listener.onReceived(null, 0, "خطا در ارتباط با سرور", true)
                        }
                    })



        }else if (method == POST){
            AndroidNetworking.post(url)
                    .addJSONObjectBody(`object`)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer " + Auth.userToken)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            var status = 0
                            var message = ""
                            try {
                                status = response.getInt("status")
                                message = response.getString("message")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            listener.onReceived(response, status, message, false)
                        }

                        override fun onError(anError: ANError?) {
                            listener.onReceived(null, 0, "خطا در ارتباط با سرور", true)
                        }
                    })
        }












//        val request = object : JsonObjectRequest(method, url, `object`, Response.Listener { response ->
//            var status = 0
//            var message = ""
//            try {
//                status = response.getInt("status")
//                message = response.getString("message")
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            listener.onReceived(response, status, message, false)
//        }, Response.ErrorListener { listener.onReceived(null, 0, "خطا در ارتباط با سرور", true) }) {
//            override fun getHeaders(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params["Accept"] = "application/json"
//                params["Content-Type"] = "application/json"
//                if (auth) params["Authorization"] = "Bearer " + Auth.userToken
//                return params
//            }
//        }
//
//
//        request
//                .setRetryPolicy(DefaultRetryPolicy(AppConfig.DEFAULT_VOLLEY_TIME_OUT_MILLIS, AppConfig.DEFAULT_VOLLEY_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
////                .setShouldCache(true)
//        val queue = Volley.newRequestQueue(context)
//        //    queue.getCache().clear();
//        queue.add(request)
    }


    fun request(method: Int, url: String, `object`: JSONObject, auth: Boolean, listener: onDataReceived, tag: String) {



        AndroidNetworking.initialize(context)
        if(method == GET) {
            AndroidNetworking.get(url)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer " + Auth.userToken)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            var status = 0
                            var message = ""
                            try {
                                status = response.getInt("status")
                                message = response.getString("message")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            listener.onReceived(response, status, message, false)
                        }

                        override fun onError(anError: ANError?) {
                            listener.onReceived(null, 0, "خطا در ارتباط با سرور", true)
                        }
                    })



        }else if (method == POST){
            AndroidNetworking.post(url)
                    .addJSONObjectBody(`object`)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer " + Auth.userToken)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            var status = 0
                            var message = ""
                            try {
                                status = response.getInt("status")
                                message = response.getString("message")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            listener.onReceived(response, status, message, false)
                        }

                        override fun onError(anError: ANError?) {
                            listener.onReceived(null, 0, "خطا در ارتباط با سرور", true)
                        }
                    })
        }













//        val request = object : JsonObjectRequest(method, url, `object`, Response.Listener { response ->
//            var status = 0
//            var message = ""
//            try {
//                status = response.getInt("status")
//                message = response.getString("message")
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            listener.onReceived(response, status, message, false)
//        }, Response.ErrorListener { listener.onReceived(null, 0, "خطا در ارتباط با سرور", true) }) {
//            override fun getHeaders(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params["Accept"] = "application/json"
//                params["Content-Type"] = "application/json"
//                if (auth) params["Authorization"] = "Bearer " + Auth.userToken
//                return params
//            }
//        }
//
//
//        request
//                .setRetryPolicy(DefaultRetryPolicy(AppConfig.DEFAULT_VOLLEY_TIME_OUT_MILLIS, AppConfig.DEFAULT_VOLLEY_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
////                .setShouldCache(true)
//        val queue = Volley.newRequestQueue(context)
//        //    queue.getCache().clear();
//        queue.add(request)
    }


    interface onDataReceived {
        fun onReceived(response: JSONObject?, status: Int, message: String, error: Boolean)
    }


}
