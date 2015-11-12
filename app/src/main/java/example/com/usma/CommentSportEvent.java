package example.com.usma;

import com.parse.ParseObject;

/**
 * Created by Arnaud Rover on 12/11/2015.
 */
public class CommentSportEvent extends ParseObject {

    public static final String AUTHOR = "author";
    public static final String TEXT = "text";
    public static final String DATE = "date";


    public void setAuthor(String author) {
        put(AUTHOR, author);
    }

    public String getAuthor() {
        return getString(AUTHOR);
    }

    public void setText(String comment) {
        put(TEXT, comment);
    }

    public void getText(String comment) {
        get(TEXT);
    }
}
