package ir.easybazi.system.pref_manager

import android.content.Context
import android.content.SharedPreferences

import ir.easybazi.system.application.Crypt

class SettingPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences


    val playVideos: Int
        get() = sharedPreferences.getInt(IS_PLAY_VIDEOS, 2)

    val fcmClientKey: String
        get() = sharedPreferences.getString(FCM_CLIENT_KEY, "")

    init {
        sharedPreferences = context.getSharedPreferences(SETTING_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun savePlayVideo(is_play: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(IS_PLAY_VIDEOS, is_play)
        editor.apply()
    }


    fun saveFcmClientKey() {
        val client_key = Crypt.generateFcmClientKey()
        val editor = sharedPreferences.edit()
        editor.putString(FCM_CLIENT_KEY, client_key)
        editor.apply()
    }

    companion object {


        private val SETTING_SHARED_PREF_NAME = "setting_pref"
        private val IS_PLAY_VIDEOS = "is_play_videos"
        private val FCM_CLIENT_KEY = "fcm_client_key"
    }


}
