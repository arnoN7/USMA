package example.com.usma;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentSportEvent extends ListFragment{

    public static final String MODIFY_EVENT_TAG = "modifyEventTag";
    private NavigationMenu sportEventsType;

    public static ListFragmentSportEvent newInstance(NavigationMenu sportEventType) {
        ListFragmentSportEvent fragment = new ListFragmentSportEvent();
        fragment.sportEventsType = sportEventType;
        return fragment;
    }

    @Override
    public void newItemAction() {
        //update the main content by replacing fragments
        startSportEventFragment(getFragmentManager(), sportEventsType, getActivity(), null);
    }

    private static void startSportEventFragment(FragmentManager fragmentManager,
                                               NavigationMenu sportEventsType, Activity activity,
                                                Bundle args) {
        //update the main content by replacing fragments
        final Fragment fragment = NewSportEvent.newInstance(sportEventsType);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (args != null) {
            fragment.setArguments(args);
        }
        if(sportEventsType == NavigationMenu.RACES) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_NEW_RACE);
            ((MainActivity)activity).setCurrentFragmentTag(MainActivity.LIST_FRAGMENT_NEW_RACE);
            ft.addToBackStack(MainActivity.LIST_FRAGMENT_NEW_RACE);
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_NEW_TRAINING);
            ((MainActivity)activity).setCurrentFragmentTag(MainActivity.
                    LIST_FRAGMENT_NEW_TRAINING);
            ft.addToBackStack(MainActivity.LIST_FRAGMENT_NEW_RACE);
        }
        ft.commit();
    }

    public void modifyItem(int position) {
        Bundle args = new Bundle();
        args.putInt(ListFragmentSportEvent.MODIFY_EVENT_TAG, position);
        startSportEventFragment(getFragmentManager(), sportEventsType, getActivity(), args);
    }

    @Override
    public void consultItemAction(int position) {
        //update the main content by replacing fragments
        final Fragment fragment = ConsultSportEvent.
                newInstance(position, sportEventsType);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(sportEventsType == NavigationMenu.RACES) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_CONSULT_RACE);
            ((MainActivity)getActivity()).
                    setCurrentFragmentTag(MainActivity.LIST_FRAGMENT_CONSULT_RACE);
            ft.addToBackStack(MainActivity.LIST_FRAGMENT_CONSULT_RACE);
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_CONSULT_TRAINING);
            ((MainActivity)getActivity()).setCurrentFragmentTag(MainActivity.
                    LIST_FRAGMENT_CONSULT_TRAINING);
            ft.addToBackStack(MainActivity.LIST_FRAGMENT_CONSULT_TRAINING);
        }
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ListFragmentSportEvent) ((MainActivity) getActivity()).
                getLoadedRootFragment(sportEventsType)).notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        List<SportEvent> sportEvents;
        if(sportEventsType == NavigationMenu.RACES) {
            mAdapter = new ListItemAdapter.ListItemAdapterRace(getActivity());
            sportEvents = ((MainActivity)getActivity()).getSportEvent(NavigationMenu.RACES);
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            mAdapter = new ListItemAdapter.ListItemAdapterTraining(getActivity());
            sportEvents = ((MainActivity)getActivity()).getSportEvent(NavigationMenu.TRAINING);
        } else {
            throw new RuntimeException("Illegal NavigationMenu item in SportEvent "
                    + getString(sportEventsType.getNameID()));
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getLayoutManager().scrollToPosition(getNextEventPosition(sportEvents));
    }

    @Override
    public void specialClose() {
        //Nothing its a root fragment
    }

    private int getNextEventPosition(List<SportEvent> sportEvents) {
        Calendar cal = null;
        for (int i = 0; i < sportEvents.size(); i++) {
            cal = USMAApplication.DateToCalendar(sportEvents.get(i).getDate());
            if (cal.after(Calendar.getInstance())) {
                return i;
            }
        }
        return (sportEvents.size() - 1);
    }
}
