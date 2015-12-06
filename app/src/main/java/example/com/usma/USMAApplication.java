package example.com.usma;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Arnaud Rover on 13/09/15.
 */
public class USMAApplication extends Application{
    private static Context context;
    public static final String NOTIF_GROUP = "group";
    public static final String NOTIF_CHANNEL = "channels";
    public static final String NOTIF_JOINEDEVENTS = "event";
    public static final String InstallationID = "objectId";
    public static final String MESSAGE = "alert";
    public static final String NEW_EVENT = "new";


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // add todo's subclass
        ParseObject.registerSubclass(SportEvent.class);
        ParseObject.registerSubclass(CommentSportEvent.class);

        // enable the Local Datastore
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "MGT2MkcrSNgKQU56BuFE7CsJUt74q79mbFAXD7c9", "Z9zTcIVDu4Qmjs6YrEwSuiB6Cv3RpptN4dv1zRnz");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
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

    public static void sendPUSHNotifToGroups(List<ParseRole> groups, String message,
                                             NavigationMenu type, SportEvent sportEvent) {
        //LinkedList<String> channels = new LinkedList<>();
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        if(groups== null) {
            return;
        }
        if(groups.size() == 0) {
            return;
        }
        for (ParseRole group: groups
                ) {
            query.whereEqualTo(NOTIF_CHANNEL, USMAApplication.NOTIF_GROUP + group.getObjectId());
            //channels.add(USMAApplication.NOTIF_GROUP+group.getObjectId());
        }
        query.whereNotEqualTo(InstallationID, ParseInstallation.getCurrentInstallation().getObjectId());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            data.put(MESSAGE, message);
            data.put(SportEvent.ObjectID, sportEvent.getObjectId());
            data.put(SportEvent.DATE, formatter.format(sportEvent.getDate()));
            data.put(SportEvent.DETAILS, sportEvent.getAddress());
            data.put(SportEvent.DESCRIPTION, sportEvent.getDescription());
            data.put(SportEvent.NAME, sportEvent.getName());
            SportsEventType sportsEventType = sportEvent.getSportEventType(getContext().getResources()
                    , type);
            data.put(SportEvent.SPORT_TYPE, sportsEventType.getSportEventType(context.
                    getResources()));
            data.put(SportEvent.MENU_TYPE, getContext().getString(type.getNameID()));
            data.put(SportEvent.CreatedAt, formatter.format(sportEvent.getCreatedAt()));
            data.put(SportEvent.UpdatedAt, formatter.format(sportEvent.getCreatedAt()));
            data.put(NEW_EVENT, "true");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        push.setData(data);
        push.setQuery(query);
        //push.setChannels(channels);
        push.sendInBackground();
        //sendPUSHToChannels(message, channels);
    }

    public static void sendPUSHNotifToEvent(SportEvent event, String message,
                                            NavigationMenu type, String objectID) {
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereEqualTo(NOTIF_CHANNEL, USMAApplication.NOTIF_JOINEDEVENTS + event.getObjectId());
        query.whereNotEqualTo(InstallationID, ParseInstallation.getCurrentInstallation().getObjectId());
        push.setQuery(query);
        push.setChannel(USMAApplication.NOTIF_JOINEDEVENTS + event.getObjectId());
        try {
            data.put(MESSAGE, message);
            data.put(SportEvent.ObjectID, objectID);
            data.put(SportEvent.MENU_TYPE, getContext().getString(type.getNameID()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        push.setData(data);
        push.sendInBackground();
    }

    private static void sendPUSHToChannels(String message, LinkedList<String> channels) {
        ParsePush push = new ParsePush();
        push.setChannels(channels);
        push.setMessage(message);
        push.sendInBackground();
    }

}
