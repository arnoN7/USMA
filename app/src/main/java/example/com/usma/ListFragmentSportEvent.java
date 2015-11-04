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

    private List<SportEvent> sportEvents;
    private NavigationMenu sportEventsType;

    public static ListFragmentSportEvent newInstance(NavigationMenu sportEventType) {
        ListFragmentSportEvent fragment = new ListFragmentSportEvent();
        fragment.sportEventsType = sportEventType;
        return fragment;
    }

    @Override
    protected List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        if(sportEventsType == NavigationMenu.TRAINING) {
            sportEvents = ((MainActivity)getActivity()).getTrainings();
        } else if (sportEventsType == NavigationMenu.RACES) {
            sportEvents = ((MainActivity)getActivity()).getRaces();
        } else {
            //should not append
            return null;
        }

        if(sportEventsType != null) {
            for (SportEvent sportEvent : sportEvents
                    ) {
                Item item = new Item(sportEventsType, sportEvent.getName(),
                        sportEvent.getDescription());
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public void newItemAction() {
        //update the main content by replacing fragments
        final Fragment fragment = NewSportEvent.newInstance(sportEventsType);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("tag");
        ft.commit();
    }

    public void addSportEvent(SportEvent newTraining, NavigationMenu navigationMenu) {
        if(navigationMenu == NavigationMenu.RACES ||
                navigationMenu == NavigationMenu.TRAINING) {
            sportEvents.add(newTraining);
            super.addItem(new Item(navigationMenu, newTraining.getName(),
                    newTraining.getDescription()));
        }
    }
}
