package example.com.usma;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Arnaud Rover on 05/12/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver{
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    private final String TAG = "PUSH_NOTIF";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive triggered!");

        JSONObject pushData;
        String alert = null;
        String title = null;
        String new_event;
        try {
            pushData = new JSONObject(intent.getStringExtra(PushReceiver.KEY_PUSH_DATA));
            alert = pushData.getString("alert");
            title = "USMA";
            new_event = pushData.getString(USMAApplication.NEW_EVENT);
            if(new_event != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SportEvent newSportEvent = new SportEvent();
                newSportEvent.setObjectId(pushData.getString(SportEvent.ObjectID));
                newSportEvent.setAddress(pushData.getString(SportEvent.DETAILS));
                try {
                    newSportEvent.setDate(formatter.parse(pushData.getString(SportEvent.DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newSportEvent.setDescription(pushData.getString(SportEvent.DESCRIPTION));
                newSportEvent.setName(pushData.getString(SportEvent.NAME));
                newSportEvent.setSportType(pushData.getString(SportEvent.SPORT_TYPE));
                newSportEvent.setType(pushData.getString(SportEvent.MENU_TYPE));
                newSportEvent.setACL(new ParseACL(ParseUser.getCurrentUser()));
                try {
                    newSportEvent.pin();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                Intent cIntent = new Intent(PushReceiver.ACTION_PUSH_OPEN);
                cIntent.putExtra(SportEvent.ObjectID, pushData.getString(SportEvent.ObjectID));
                cIntent.putExtra(SportEvent.MENU_TYPE, pushData.getString(SportEvent.MENU_TYPE));
                cIntent.putExtras(intent.getExtras());
                cIntent.setPackage(context.getPackageName());

                // WE SHOULD HANDLE DELETE AS WELL
                // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

                PendingIntent pContentIntent =
                        PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(alert)
                        .setContentText(title)
                        .setContentIntent(pContentIntent)
                        .setAutoCancel(true).setSmallIcon(R.drawable.logousmawhite);


                NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                myNotificationManager.notify(1, builder.build());
            }
        } catch (JSONException e) {}


    }
}
