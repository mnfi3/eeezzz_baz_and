package sabalan.paydar.mohtasham.ezibazi.system.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import sabalan.paydar.mohtasham.ezibazi.BuildConfig
import sabalan.paydar.mohtasham.ezibazi.api_service.account.LoginCheckerService
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.model.RentType
import sabalan.paydar.mohtasham.ezibazi.model.User
import sabalan.paydar.mohtasham.ezibazi.system.hardware.ConnectivityListener
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.CityPrefManager


class G : Application() {

    companion object {

        lateinit var context: Context
        lateinit var connectivityListener: ConnectivityListener
        var mustReconnect = false
        var isLoggedIn = false
        var rentTypes: List<RentType> ? = null


        fun initializeLogin(){
            val loginCheckerService = LoginCheckerService(context)
            loginCheckerService.check(User(), object : LoginCheckerService.onLoginCheckComplete{
                override fun onComplete(isLoggedIn: Boolean, full_name: String) {
                    G.isLoggedIn = isLoggedIn
                }
            })
        }
    }

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        Fabric.with(this, crashlyticsKit)
        Fabric.with(this, Answers())


        G.context = applicationContext


        initializeLogin()

        getRentTypes()
        setFakeCity()
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
        CityPrefManager(context).cityId = 14
    }




    private fun getRentTypes() {
        val service = RentService(context)
        service.getRentTypes(object : RentService.onRentTypesReceived {
            override fun onReceived(status: Int, message: String, rentTypes: List<RentType>) {
                if (status == 1) {
                    G.rentTypes = rentTypes
                }else{
//                        G.getRentTypes();
                }
            }
        })
    }











}