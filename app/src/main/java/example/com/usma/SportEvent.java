package example.com.usma;

import android.content.res.Resources;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

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

    public SportsEventRaceType getSportEventRaceType(Resources resources) {
        String sSportType = getString(SPORT_TYPE);
        return SportsEventRaceType.getSportTypeByName(sSportType, resources);
    }



}
