package example.com.usma;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Rover on 19/10/15.
 */
public class User {
    public static final String NAME = "name";
    public static final String FIRSTNAME = "firstname";
    public static final String BIRTHDATE = "birthdate";
    public static final String LICENCE = "licence";
    public static final String CHANNELS = "channels";


    public static boolean isEventJoined(SportEvent event) {
        List<String> channels = ParseInstallation.getCurrentInstallation().getList("channels");
        if(channels != null) {
            for (String channel : channels
                    ) {
                if (channel.equals(USMAApplication.NOTIF_JOINEDEVENTS + event.getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void subscribeToEvent(SportEvent event) {
        ParsePush.subscribeInBackground(USMAApplication.NOTIF_JOINEDEVENTS + event.getObjectId());
    }

    public static void unsubscribeToEvent(SportEvent event) {
        ParsePush.unsubscribeInBackground(USMAApplication.NOTIF_JOINEDEVENTS + event.getObjectId());

    }
}
