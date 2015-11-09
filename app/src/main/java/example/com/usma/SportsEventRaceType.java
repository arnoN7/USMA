package example.com.usma;

import android.content.res.Resources;

/**
 * Created by Arnaud Rover on 09/11/2015.
 */
public enum SportsEventRaceType {
    ROAD(R.array.array_race_type, R.drawable.road, R.drawable.roadicon, 0),
    CROSS(R.array.array_race_type, R.drawable.cross, R.drawable.crossicon, 1),
    TRAIL(R.array.array_race_type, R.drawable.trail, R.drawable.trailicon, 2),
    ATHLE(R.array.array_race_type, R.drawable.athle, R.drawable.athlesmall, 3);

    private int arrayID;
    private int imageID;
    private int iconID;
    private int position;

    private SportsEventRaceType(int arrayID, int imageID, int iconID, int position) {
        this.arrayID = arrayID;
        this.imageID = imageID;
        this.iconID = iconID;
        this.position = position;
    }

    public static SportsEventRaceType getSportTypeByName(String name, Resources resources) {
        SportsEventRaceType result = null;
        String[] sportsType = resources.getStringArray(R.array.array_race_type);
        for (int i = 0; i < sportsType.length; i++) {
            if(sportsType[i].equals(name)) {
                return SportsEventRaceType.values()[i];
            }
        }
        return result;
    }

    public int getImageID() {
        return imageID;
    }
}
