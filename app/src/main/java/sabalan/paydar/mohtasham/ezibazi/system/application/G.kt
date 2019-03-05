package sabalan.paydar.mohtasham.ezibazi.system.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics

import sabalan.paydar.mohtasham.ezibazi.BuildConfig

import io.fabric.sdk.android.Fabric

import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.system.auth.Auth
import sabalan.paydar.mohtasham.ezibazi.system.hardware.ConnectivityListener
import sabalan.paydar.mohtasham.ezibazi.system.hardware.Storage
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.CityPrefManager
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.UserPrefManager
import sabalan.paydar.mohtasham.ezibazi.model.RentType
import sabalan.paydar.mohtasham.ezibazi.model.User


class G : Application() {

    //analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null


    override fun onCreate() {
        super.onCreate()
        //    Crashlytics crashlyticsKit = new Crashlytics.Builder()
        //      .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        //      .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        //    Fabric.with(this, crashlyticsKit);
        Fabric.with(this, Crashlytics())
        Fabric.with(this, Answers())


        context = applicationContext
        initializeLogin()
        setFakeCity()
        getRentTypes()
        //    Storage.deleteCache(context);
        connectivityListener = ConnectivityListener()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        appOpenAnalytics()
    }

    @SuppressLint("InvalidAnalyticsName")
    private fun appOpenAnalytics() {
        val bundle = Bundle()
        bundle.putString("APP_OPEN", "1")
        mFirebaseAnalytics!!.logEvent("App", bundle)
    }

    private fun setFakeCity() {
        //    new CityPrefManager(context).setCityId(AppConfig.DEFAULT_CITY_ID);
        CityPrefManager(context).cityId = 329
    }


    interface onLoginCheck {
        fun onCheck(user: User, isLoggedIn: Boolean)
    }

    companion object {

        //  getString(R.string.default_notification_channel_id);

        @SuppressLint("StaticFieldLeak")
        var context: Context
        val TAG = " G "

        var connectivityListener: ConnectivityListener
        var mustReconnect = false


        var isLoggedIn = false
        var rentTypes: List<RentType>

        private fun getRentTypes() {
            val service = RentService(context)
            service.getRentTypes(object : RentService.onRentTypesReceived {
                override fun onReceived(status: Int, message: String, rentTypes: List<RentType>) {
                    if (status == 1) {
                        G.rentTypes = rentTypes
                    }
                }
            })
        }


        fun initializeLogin() {
            Auth.loginCheck(object : onLoginCheck {
                override fun onCheck(user: User, isLoggedIn: Boolean) {
                    G.isLoggedIn = isLoggedIn
                    if (isLoggedIn) {
                        val prefManager = UserPrefManager(context)
                        prefManager.saveUserFullName(user.full_name)
                    }
                }
            })
        }
    }

}