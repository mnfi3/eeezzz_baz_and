package ir.easybazi.system.pref_manager

import android.content.Context
import android.content.SharedPreferences

import ir.easybazi.model.Fcm

class FcmPrefManager(context: Context) {


    private val sharedPreferences: SharedPreferences


    val fcm: Fcm
        get() {
            val fcm = Fcm()
            val token = sharedPreferences.getString(FCM_TOKEN, "")
            val client_key = sharedPreferences.getString(FCM_CLIENT_KEY, "")
            fcm.token = token
            fcm.client_key = client_key
            return fcm
        }

    init {
        sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveFcm(fcm: Fcm) {
        val newToken = fcm.token
        val newClientKey = fcm.client_key
        val editor = sharedPreferences.edit()
        editor.putString(FCM_TOKEN, newToken)
        editor.putString(FCM_CLIENT_KEY, newClientKey)
        editor.apply()
    }

    companion object {

        private val TAG = "FcmPrefManager"


        private val USER_SHARED_PREF_NAME = "fcm_shared_pref"
        private val FCM_TOKEN = "fcm_token"
        private val FCM_CLIENT_KEY = "fcm_client_key"
    }


}
