//package ir.easybazi.firebase

//class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
//
//
//    @SuppressLint("LongLogTag")
//    override fun onTokenRefresh() {
//        // Get updated InstanceID token.
//        val refreshedToken = FirebaseInstanceId.getInstance().token
//        //    Log.d(TAG, "Refreshed token: " + refreshedToken);
//        val fcm = Fcm()
//        fcm.token = refreshedToken
//        fcm.client_key = SettingPrefManager(this@MyFirebaseInstanceIDService).fcmClientKey
//        val fcmPrefManager = FcmPrefManager(this@MyFirebaseInstanceIDService)
//        fcmPrefManager.saveFcm(fcm)
//        sendFcmInfoToServer(fcm)
//    }
//
//
//    private fun sendFcmInfoToServer(fcm: Fcm) {
//        //    Fcm fcm = new FcmPrefManager(MyFirebaseInstanceIDService.this).getFcm();
//        val `object` = JSONObject()
//        try {
//            `object`.put("device_type", "ANDROID")
//            `object`.put("fcm_token", fcm.token)
//            `object`.put("client_key", fcm.client_key)
//        } catch (e: JSONException) {
//        }
//
//        val service = FireBaseApiService(this@MyFirebaseInstanceIDService)
//        service.refreshFcmToken(`object`, object : FireBaseApiService.onRefreshTokenReceived {
//            override fun onReceived(status: Int, message: String, device: Device) {}
//        })
//    }
//
//    companion object {
//        private val TAG = "MyFirebaseInstanceIDService"
//    }
//
//}
