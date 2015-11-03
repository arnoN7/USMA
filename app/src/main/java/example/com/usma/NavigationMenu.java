package example.com.usma;

import android.content.Context;

/**
 * Created by Arnaud Rover on 16/10/15.
 */
public enum NavigationMenu {


    RACES(R.string.race, R.drawable.ic_directions_run_black_24dp, 0),
    TRAINING(R.string.training, R.drawable.ic_alarm_black_24dp, 1),
    GROUPS(R.string.groups, R.drawable.ic_group_black_24dp, 2),
    LICENCE(R.string.licence, R.drawable.ic_folder_shared_black_24dp, 3),
    USERS(R.string.users, R.drawable.ic_person_black_24dp, 4);

    private int nameID;
    private int imageID;
    private int id;

    private NavigationMenu(int nameID, int imageID, int id) {
        this.nameID = nameID;
        this.imageID = imageID;
        this.id = id;
    }

    public int getNameID() {
        return nameID;
    }

    public int getImageID() {
        return imageID;
    }

    public int getId() {
        return id;
    }

    public static NavigationMenu getNavigationMenuByID (int id) {
        if (id == RACES.id) {
            return RACES;
        } else if (id == TRAINING.id) {
            return TRAINING;
        } else if (id == GROUPS.id) {
            return GROUPS;
        } else if (id == LICENCE.id) {
            return LICENCE;
        } else if (id == USERS.id) {
            return USERS;
        } else {
            return null;
        }
    }

    public static NavigationMenu getNavigationIDByString(String nameMenu, Context context) {
        if (context.getString(RACES.nameID).equals(nameMenu)) {
            return RACES;
        } else if (context.getString(TRAINING.nameID).equals(nameMenu)) {
            return TRAINING;
        } else if (context.getString(GROUPS.nameID).equals(nameMenu)) {
            return GROUPS;
        } else if (context.getString(LICENCE.nameID).equals(nameMenu)) {
            return LICENCE;
        } else if (context.getString(USERS.nameID).equals(nameMenu)) {
            return USERS;
        } else {
            return null;
        }
    }
}
