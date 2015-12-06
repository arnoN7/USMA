package example.com.usma;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseRole;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Arnaud Rover on 29/10/2015.
 */
public class GroupUsers {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ADMIN = "admin";

    public static void subscribeToGroup(ParseRole group) {
        ParsePush.subscribeInBackground(USMAApplication.NOTIF_GROUP + group.getObjectId());
    }

    public static void unsubscribeToGroup(ParseRole group) {
        ParsePush.unsubscribeInBackground(USMAApplication.NOTIF_GROUP + group.getObjectId());
    }
}
