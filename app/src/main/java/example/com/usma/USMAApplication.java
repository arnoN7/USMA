package example.com.usma;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
        //ParseObject.registerSubclass(Todo.class);



        // enable the Local Datastore
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "MGT2MkcrSNgKQU56BuFE7CsJUt74q79mbFAXD7c9", "Z9zTcIVDu4Qmjs6YrEwSuiB6Cv3RpptN4dv1zRnz");
        ParseUser.enableRevocableSessionInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);


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
}
