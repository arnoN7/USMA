package example.com.usma;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arnaud Rover on 13/09/15.
 */
public class USMAApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // add todo's subclass
        ParseObject.registerSubclass(SportEvent.class);




        // enable the Local Datastore
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "MGT2MkcrSNgKQU56BuFE7CsJUt74q79mbFAXD7c9", "Z9zTcIVDu4Qmjs6YrEwSuiB6Cv3RpptN4dv1zRnz");
        //ParseUser.enableRevocableSessionInBackground();
        //ParseUser.enableAutomaticUser();
        //ParseACL defaultACL = new ParseACL();
        //ParseACL.setDefaultACL(defaultACL, true);


        /*ParsePush.("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });*/
    }

    public static Context getContext() {
        return context;
    }

    public static void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
