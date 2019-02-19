package sabalan.paydar.mohtasham.ezibazi.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityMenu;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowRent;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowShop;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityTicket;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityWebView;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


  public MyFirebaseMessagingService() {
  }

  private final String TAG = "MyFirebaseMessagingService";

  private static final String ACTIVITY_MENU = "activity_menu";
  private static final String ACTIVITY_WEB_VIEW = "activity_web_view";
  private static final String ACTIVITY_SHOW_RENT = "activity_show_rent";
  private static final String ACTIVITY_SHOW_SHOP = "activity_show_shop";
  private static final String ACTIVITY_TICKET = "activity_ticket";

  private static final String DESTINATION="destination";
  private static final String CLICK_ACTION="click_action";

  private static final String NOTIFICATION_DATA_TYPE_URL = "url";
  private static final String NOTIFICATION_DATA_TYPE_ID = "id";


  private Intent intent;


  @SuppressLint("LongLogTag")
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // ...


    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());

      Map<String, String> data = remoteMessage.getData();
      String destination = data.get(DESTINATION);
      switch (destination){

//        case ACTIVITY_MENU :
////          intent = new Intent(this,ActivityMenu.class);
////          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////          sendNotification(remoteMessage.getNotification().getBody(), intent);
////          /*example of firebase message :
////          * destination = activity_menu
////          * */
////          break;

        case ACTIVITY_WEB_VIEW :
          String url = data.get(NOTIFICATION_DATA_TYPE_URL);
          intent = new Intent(MyFirebaseMessagingService.this, ActivityWebView.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("URL", url);
          sendNotification(remoteMessage.getNotification().getBody(), intent);
          /*example of firebase message :
           * destination = activity_web_view
           * url = www.google.com
           * */
          break;

        case ACTIVITY_SHOW_RENT :
          int game_for_rent_id = Integer.valueOf(data.get(NOTIFICATION_DATA_TYPE_ID));
          intent = new Intent(MyFirebaseMessagingService.this, ActivityShowRent.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("ID", game_for_rent_id);
          sendNotification(remoteMessage.getNotification().getBody(), intent);
          /*example of firebase message :
           * destination = activity_show_rent
           * id = 1
           * */
          break;

        case ACTIVITY_SHOW_SHOP :
          int game_for_shop_id = Integer.valueOf(data.get(NOTIFICATION_DATA_TYPE_ID));
          intent = new Intent(MyFirebaseMessagingService.this, ActivityShowShop.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("ID", game_for_shop_id);
          sendNotification(remoteMessage.getNotification().getBody(), intent);
          /*example of firebase message :
           * destination = activity_show_shop
           * id = 1
           * */
        break;

        case ACTIVITY_TICKET :
          intent = new Intent(MyFirebaseMessagingService.this, ActivityTicket.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          sendNotification(remoteMessage.getNotification().getBody(), intent);
          /*example of firebase message :
           * destination = activity_ticket
           * */
          break;


      }



      if (/* Check if data needs to be processed by long running job */ true) {
        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//        scheduleJob();
      } else {
        // Handle message within 10 seconds
//        handleNow();
      }

    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
  }


  private void sendNotification(String messageBody, Intent intent) {
    Intent intent1 = new Intent(this, ActivityMenu.class);
//    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent1);

//    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//      PendingIntent.FLAG_ONE_SHOT);
      PendingIntent.FLAG_UPDATE_CURRENT);
//
    String channelId = getString(R.string.default_notification_channel_id);
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder =
      new NotificationCompat.Builder(this, channelId)
//      new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_ezibazi)
//        .setContentTitle(getString(R.string.fcm_message))
        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.gamepad))
        .setContentTitle(getString(R.string.app_name))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);
//
    NotificationManager notificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId,
//        "Channel human readable title",
        "EziBazi",
        NotificationManager.IMPORTANCE_DEFAULT);
      assert notificationManager != null;
      notificationManager.createNotificationChannel(channel);
    }

    assert notificationManager != null;
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }



}
