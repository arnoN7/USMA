package example.com.usma;

import com.parse.FindCallback;
import com.parse.ParseException;
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
    public static final String JOINEDEVENTS = "joined_events";

    public static boolean isEventJoined(SportEvent event) {
        List<SportEvent> joinedEvents = MainActivity.getLoadedJoinedEvents();
        for (SportEvent joinedEvent : joinedEvents
                ) {
            if(joinedEvent.getObjectId().equals(event.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public static void addJoinedEvent(SportEvent event) {
        ParseRelation<SportEvent> relation = ParseUser.getCurrentUser().
                getRelation(User.JOINEDEVENTS);
        relation.add(event);
        ParseUser.getCurrentUser().saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                MainActivity.setJoignedEvents();
            }
        });
    }

    public static void removeJoinedEvent(SportEvent event) {
        ParseRelation<SportEvent> relation = ParseUser.getCurrentUser().
                getRelation(User.JOINEDEVENTS);
        relation.remove(event);
        ParseUser.getCurrentUser().saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                MainActivity.setJoignedEvents();
            }
        });
    }
}
