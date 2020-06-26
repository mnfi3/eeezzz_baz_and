package ir.easybazi.system.pref_manager


import android.content.Context
import android.content.SharedPreferences
import ir.easybazi.model.User
import ir.easybazi.system.application.Crypt

class UserPrefManager(context: Context) {

    //  private static final String CLIENT_KEY = "client_key";

    private val sharedPreferences: SharedPreferences

    //    user.setClient_key(sharedPreferences.getString(CLIENT_KEY , ""));
    val user: User
        get() {
            val user = User()
            user.full_name = sharedPreferences.getString(FULL_NAME, "")
            val main_token = Crypt.decrypt(sharedPreferences.getString(TOKEN, ""))
            user.token = main_token
            return user
        }

    init {
        sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(user: User?) {
        if (user != null) {
            val newToken = Crypt.encrypt(user.token!!)
            user.token = newToken
            val editor = sharedPreferences.edit()
            editor.putString(FULL_NAME, user.full_name)
            editor.putString(TOKEN, user.token)
            //      editor.putString(CLIENT_KEY, user.getClient_key());
            editor.apply()
        }
    }

    fun saveUserFullName(f_name: String?) {
        var full_name = ""
        if (f_name != null) {
            full_name = f_name
            val editor = sharedPreferences.edit()
            editor.putString(FULL_NAME, full_name)
            editor.apply()
        }


    }

    companion object {

        private val TAG = "UserSharedPrefManager"


        private val USER_SHARED_PREF_NAME = "user_shared_pref"
        private val FULL_NAME = "name"
        private val TOKEN = "token"
    }


}
