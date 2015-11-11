package example.com.usma;

import android.content.res.Resources;

/**
 * Created by Arnaud Rover on 09/11/2015.
 */
public enum SportsEventType {
    //Race SportEvent Types
    ROAD(R.array.array_race_type, R.drawable.road, R.drawable.roadicon, 0),
    CROSS(R.array.array_race_type, R.drawable.cross, R.drawable.crossicon, 1),
    TRAIL(R.array.array_race_type, R.drawable.trail, R.drawable.trailicon, 2),
    ATHLE(R.array.array_race_type, R.drawable.athle, R.drawable.athlesmall, 3),

    //Training SportEvent Types
    INTERVAL(R.array.array_training_type, R.drawable.road, R.drawable.roadicon, 0),
    ENDURANCE(R.array.array_training_type, R.drawable.cross, R.drawable.crossicon, 1),
    LONG(R.array.array_training_type, R.drawable.trail, R.drawable.trailicon, 2);

    private int arrayID;
    private int imageID;
    private int iconID;
    private int position;

    private SportsEventType(int arrayID, int imageID, int iconID, int position) {
        this.arrayID = arrayID;
        this.imageID = imageID;
        this.iconID = iconID;
        this.position = position;
    }

    public static SportsEventType getSportTypeByName(String name, Resources resources,
                                                     NavigationMenu type) {
        SportsEventType result = null;
        String[] sportsType = null;
        int offset = 0; //To get training sportEvents Type we have to add an offset to the ID to skip Race types
        if(type == NavigationMenu.RACES) {
            sportsType = resources.getStringArray(R.array.array_race_type);
        } else if (type == NavigationMenu.TRAINING) {
            sportsType = resources.getStringArray(R.array.array_training_type);
            offset = resources.getStringArray(R.array.array_race_type).length;
        }

        for (int i = 0; i < sportsType.length; i++) {
            if(sportsType[i].equals(name)) {
                return SportsEventType.values()[i + offset];
            }
        }
        return result;
    }

    public int getImageID() {
        return imageID;
    }

    public int getIconID() {
        return iconID;
    }

    public String getSportEventType(Resources resources) {
        String[] raceTypes = resources.getStringArray(this.arrayID);
        return raceTypes[this.position];
    }

    public int getPosition() {
        return position;
    }
}
