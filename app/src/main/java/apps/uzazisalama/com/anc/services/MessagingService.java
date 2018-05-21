package apps.uzazisalama.com.anc.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import apps.uzazisalama.com.anc.MainActivity;
import apps.uzazisalama.com.anc.R;
import apps.uzazisalama.com.anc.database.AncClient;
import apps.uzazisalama.com.anc.database.Referral;
import apps.uzazisalama.com.anc.utils.Config;
import apps.uzazisalama.com.anc.utils.NotificationUtils;

import static apps.uzazisalama.com.anc.base.BaseActivity.database;

public class MessagingService extends FirebaseMessagingService {

    final String TAG = "MessagingService";

    private NotificationUtils notificationUtils;

    public MessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void triggerNotification(String msg)
    {
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Htmr Facility")
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                        .setContentText(msg);

        Notification notification = builder.build();
        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    void handleDataMessage(JSONObject json){

        AncClient ancClient = new AncClient();
        Referral referral = new Referral();

        Gson gson = new Gson();

        try {
            JSONObject data = new JSONObject(json.toString());
            String type = data.getString("type");
            if (type.equals("ClientRegistration")){
                triggerNotification("New Client Registered");
                ancClient = gson.fromJson(data.getJSONObject("AncClientDTO").toString(), AncClient.class);
                database.clientModel().addNewClient(ancClient);
                Log.d("handleNotification", "added a client");
            }else if (type.equals("ClientReferral")){
                triggerNotification("New Referral Received");
                JSONObject referralDTO = new JSONObject(json.toString());
                referral = gson.fromJson(referralDTO.toString(), Referral.class);
                database.referralModelDao().addNewReferral(referral);
                Log.d("handleNotification", "added Registered Patient");

            }else if (type.equals("ReferralFeedback")){
                Log.d("handleNotification", "Received Feedback");
                triggerNotification("Referral Feedback Received");
                JSONObject ReferralsDTO = new JSONObject(json.toString());
                referral = gson.fromJson(ReferralsDTO.toString(), Referral.class);
                database.referralModelDao().addNewReferral(referral);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Log.e(TAG, "push json: " + json.toString());

        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        String msg = "Umepokea Data";

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", msg);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            //play notification sound
            //NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            //notificationUtils.playNotificationSound();
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", "");

            showNotificationMessage(getApplicationContext(), "HTMR", msg, null, resultIntent);

        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}
