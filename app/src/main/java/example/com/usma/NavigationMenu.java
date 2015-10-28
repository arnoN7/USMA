package example.com.usma;

/**
 * Created by Arnaud Rover on 16/10/15.
 */
public enum NavigationMenu {
    TRAINING(R.string.training, R.drawable.ic_alarm_black_24dp, 0),
    RACES(R.string.race, R.drawable.ic_directions_run_black_24dp, 1),
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
}
