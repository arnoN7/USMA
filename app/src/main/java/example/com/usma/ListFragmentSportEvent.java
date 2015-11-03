package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentSportEvent extends ListFragment{

    private static final String ARG_SPORT_EVENT_TYPE = "sportEventType";
    private List<SportEvent> trainings;
    private NavigationMenu sportEventType;

    public static ListFragmentSportEvent newInstance(NavigationMenu sportEventType) {
        ListFragmentSportEvent fragment = new ListFragmentSportEvent();
        fragment.sportEventType = sportEventType;
        return fragment;
    }

    @Override
    public void newItemAction() {
        //update the main content by replacing fragments
        final Fragment fragment = NewTraining.newInstance(sportEventType);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("tag");
        ft.commit();
    }

    public void setSportEvents(List<SportEvent> sportEvents) {
        this.trainings = sportEvents;
        List<Item> items = new ArrayList<>();
        if(sportEvents != null) {
            for (SportEvent training : sportEvents
                    ) {
                Item item = new Item(NavigationMenu.TRAINING, training.getName(),
                        training.getDescription());
                items.add(item);
            }
        }
        super.setItems(items);
    }

    public void addSportEvent(SportEvent newTraining, NavigationMenu navigationMenu) {
        if(navigationMenu == NavigationMenu.RACES ||
                navigationMenu == NavigationMenu.TRAINING) {
            trainings.add(newTraining);
            super.addItem(new Item(navigationMenu, newTraining.getName(),
                    newTraining.getDescription()));
        }
    }
}
