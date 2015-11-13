package example.com.usma;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Arnaud Rover on 12/11/2015.
 */
@ParseClassName("CommentSportEvent")
public class CommentSportEvent extends ParseObject {

    public static final String AUTHOR = "author";
    public static final String TEXT = "text";
    public static final String DATE = "createdAt";
    public static final String SPORT_EVENT = "sport_event";

    public CommentSportEvent() {

    }

    public void setAuthor(String author) {
        put(AUTHOR, author);
    }

    public String getAuthor() {
        return getString(AUTHOR);
    }

    public void setText(String comment) {
        put(TEXT, comment);
    }

    public String getText() {
        return getString(TEXT);
    }

    public void setSportEvent(SportEvent event) {
        put(SPORT_EVENT, event);
    }

    public SportEvent getSportEvent() {
        return (SportEvent) get(SPORT_EVENT);
    }
}
