package example.com.usma;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
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
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_NEW_TRAINING);
            ((MainActivity)activity).setCurrentFragmentTag(MainActivity.
                    LIST_FRAGMENT_NEW_TRAINING);
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
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_CONSULT_TRAINING);
            ((MainActivity)getActivity()).setCurrentFragmentTag(MainActivity.
                    LIST_FRAGMENT_CONSULT_TRAINING);
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
        if(sportEventsType == NavigationMenu.RACES) {
            mAdapter = new ListItemAdapter.ListItemAdapterRace(getActivity());
        } else if (sportEventsType == NavigationMenu.TRAINING) {
            mAdapter = new ListItemAdapter.ListItemAdapterTraining(getActivity());
        } else {
            throw new RuntimeException("Illegal NavigationMenu item in SportEvent "
                    + getString(sportEventsType.getNameID()));
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}
