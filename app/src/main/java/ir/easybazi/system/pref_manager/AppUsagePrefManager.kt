package ir.easybazi.system.pref_manager

import android.content.Context
import android.content.SharedPreferences

class AppUsagePrefManager(context: Context) {
    //  private static final String CLIENT_KEY = "client_key";

    private val sharedPreferences: SharedPreferences


    val firstUse: Int
        get() {
            val first_use = sharedPreferences.getInt(IS_FIRST_USE, 1)
            return if (first_use == 1) {
                1
            } else {
                0
            }
        }

    init {
        sharedPreferences = context.getSharedPreferences(APP_USAGE_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveFirstUse() {
        val editor = sharedPreferences.edit()
        editor.putInt(IS_FIRST_USE, 0)
        editor.apply()
    }

    companion object {


        private val APP_USAGE_SHARED_PREF_NAME = "app_usage_pref"
        private val IS_FIRST_USE = "is_first_use_v1"
    }


}
