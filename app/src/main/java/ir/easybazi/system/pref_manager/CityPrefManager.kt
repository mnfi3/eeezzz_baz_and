package ir.easybazi.system.pref_manager

import android.content.Context
import android.content.SharedPreferences

import ir.easybazi.system.config.AppConfig

class CityPrefManager(context: Context) {
    //  private static final String CLIENT_KEY = "client_key";

    private val sharedPreferences: SharedPreferences

    var cityId: Int
        get() = sharedPreferences.getInt(CITY_ID, AppConfig.DEFAULT_CITY_ID)
        set(id) {
            val editor = sharedPreferences.edit()
            editor.putInt(CITY_ID, id)
            editor.apply()
        }

    init {
        sharedPreferences = context.getSharedPreferences(CITY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {

        private val CITY_SHARED_PREF_NAME = "city_shared_pref"
        private val CITY_ID = "city_id"
    }


}
