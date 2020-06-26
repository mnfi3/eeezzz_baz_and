package ir.easybazi.system.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ir.easybazi.api_service.account.LoginCheckerService
import ir.easybazi.api_service.main.RentService
import ir.easybazi.model.RentType
import ir.easybazi.model.User
import ir.easybazi.system.hardware.ConnectivityListener
import ir.easybazi.system.pref_manager.CityPrefManager


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

//        FirebaseInstanceId.getInstance().instanceId
//                .addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w("FIREBASE", "getInstanceId failed", task.exception)
//                        return@OnCompleteListener
//                    }
//                    val token = task.result?.token
////                    val msg = getString(R.string.msg_token_fmt, token)
//                    Log.d("FIREBASE", token)
//                    Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//                })

//        val crashlyticsKit = Crashlytics.Builder()
//                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                .build()
//        Fabric.with(this, crashlyticsKit)
//        Fabric.with(this, Answers())


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