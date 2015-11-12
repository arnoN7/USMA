package example.com.usma;

import android.content.res.Resources;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arnaud Rover on 30/10/2015.
 */
@ParseClassName("SportEvent")
public class SportEvent extends ParseObject {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String MENU_TYPE = "type";
    public static final String DETAILS = "address";
    public static final String SPORT_TYPE = "sport_type";
    public static final String GROUPS = "groups";
    public static final String COMMENTLIST = "comments";
    private ParseRelation<ParseRole> relation;

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public void setDescription (String description) {
        put(DESCRIPTION, description);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public void setDate(Date date) {
        put(DATE, date);
    }

    public Date getDate() {
        return getDate(DATE);
    }

    public void setType(String type){
        put(MENU_TYPE, type);
    }

    public NavigationMenu getType(Resources resources) {
        String sType = getString(MENU_TYPE);
        return NavigationMenu.getNavigationIDByString(sType, resources);

    }

    public void setAddress (String address) {
        put(DETAILS, address);
    }

    public String getAddress() {
        return getString(DETAILS);
    }

    public void setSportType(String sportType) {
        put(SPORT_TYPE, sportType);
    }

    public SportsEventType getSportEventType(Resources resources, NavigationMenu navigationMenu) {
        String sSportType = getString(SPORT_TYPE);
        return SportsEventType.getSportTypeByName(sSportType, resources, navigationMenu);
    }
    public void addGroup(ParseRole group) {
        getRelation(GROUPS).add(group);
    }
    public ParseRelation<ParseRole> getGroupsRelation() {
        return getRelation(GROUPS);
    }
    public List<ParseRole> getGroups(boolean  refresh) {
        try {
            ParseQuery<ParseRole> query = getGroupsRelation().getQuery();
            if (!refresh) {
                query = query.fromLocalDatastore();
            }
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void addComment(CommentSportEvent comment) {
        getComments().add(comment);
    }

    public ArrayList<CommentSportEvent> getComments() {
        return (ArrayList<CommentSportEvent>) get(COMMENTLIST);
    }




}
