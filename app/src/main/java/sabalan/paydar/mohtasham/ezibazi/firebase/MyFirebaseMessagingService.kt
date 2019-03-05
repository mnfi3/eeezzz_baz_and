package sabalan.paydar.mohtasham.ezibazi.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityMenu
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowRent
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowShop
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityTicket
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityWebView


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"


    private var intent: Intent? = null


    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val data = remoteMessage.data
            val destination = data[DESTINATION]
            when (destination) {

                //        case ACTIVITY_MENU :
                ////          intent = new Intent(this,ActivityMenu.class);
                ////          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ////          sendNotification(remoteMessage.getNotification().getBody(), intent);
                ////          /*example of firebase message :
                ////          * destination = activity_menu
                ////          * */
                ////          break;

                ACTIVITY_WEB_VIEW -> {
                    val url = data[NOTIFICATION_DATA_TYPE_URL]
                    intent = Intent(this@MyFirebaseMessagingService, ActivityWebView::class.java)
                    //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent!!.putExtra("URL", url)
                    sendNotification(remoteMessage.notification!!.body, intent!!)
                }

                ACTIVITY_SHOW_RENT -> {
                    val game_for_rent_id = Integer.valueOf(data[NOTIFICATION_DATA_TYPE_ID])
                    intent = Intent(this@MyFirebaseMessagingService, ActivityShowRent::class.java)
                    //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent!!.putExtra("ID", game_for_rent_id)
                    sendNotification(remoteMessage.notification!!.body, intent!!)
                }

                ACTIVITY_SHOW_SHOP -> {
                    val game_for_shop_id = Integer.valueOf(data[NOTIFICATION_DATA_TYPE_ID])
                    intent = Intent(this@MyFirebaseMessagingService, ActivityShowShop::class.java)
                    //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent!!.putExtra("ID", game_for_shop_id)
                    sendNotification(remoteMessage.notification!!.body, intent!!)
                }

                ACTIVITY_TICKET -> {
                    intent = Intent(this@MyFirebaseMessagingService, ActivityTicket::class.java)
                    //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendNotification(remoteMessage.notification!!.body, intent!!)
                }
            }/*example of firebase message :
           * destination = activity_web_view
           * url = www.google.com
           * *//*example of firebase message :
           * destination = activity_show_rent
           * id = 1
           * *//*example of firebase message :
           * destination = activity_show_shop
           * id = 1
           * *//*example of firebase message :
           * destination = activity_ticket
           * */



            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //        scheduleJob();
            } else {
                // Handle message within 10 seconds
                //        handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private fun sendNotification(messageBody: String?, intent: Intent) {
        val intent1 = Intent(this, ActivityMenu::class.java)
        //    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1)

        //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                //      PendingIntent.FLAG_ONE_SHOT);
                PendingIntent.FLAG_UPDATE_CURRENT)
        //
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                //      new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_ezibazi)
                //        .setContentTitle(getString(R.string.fcm_message))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.gamepad))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        //
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    //        "Channel human readable title",
                    "EziBazi",
                    NotificationManager.IMPORTANCE_DEFAULT)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }

        assert(notificationManager != null)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private val ACTIVITY_MENU = "activity_menu"
        private val ACTIVITY_WEB_VIEW = "activity_web_view"
        private val ACTIVITY_SHOW_RENT = "activity_show_rent"
        private val ACTIVITY_SHOW_SHOP = "activity_show_shop"
        private val ACTIVITY_TICKET = "activity_ticket"

        private val DESTINATION = "destination"
        private val CLICK_ACTION = "click_action"

        private val NOTIFICATION_DATA_TYPE_URL = "url"
        private val NOTIFICATION_DATA_TYPE_ID = "id"
    }


}
