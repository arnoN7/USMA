package example.com.usma;

import android.widget.ImageView;

/**
 * Created by Arnaud Rover on 16/10/15.
 */
public enum NavigationMenu {
    TRAINING(R.string.training, R.drawable.ic_alarm_black_24dp),
    RACES(R.string.race, R.drawable.ic_directions_run_black_24dp),
    GROUPS(R.string.groups, R.drawable.ic_group_black_24dp),
    LICENCE(R.string.licence, R.drawable.ic_folder_shared_black_24dp);

    private int nameID;
    private int imageID;

    private NavigationMenu(int nameID, int imageID) {
        this.nameID = nameID;
        this.imageID = imageID;
    }

    public int getNameID() {
        return nameID;
    }

    public int getImageID() {
        return imageID;
    }
}
