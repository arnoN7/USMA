package example.com.usma;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultSportEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultSportEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultSportEvent extends Fragment {

    private static final String CURRENT_POSITION_TAG = "currentFragmentTag";
    private static final String CURRENT_SPORT_TYPE_TAG = "currentTitleTag";

    private OnFragmentInteractionListener mListener;
    private TextView mTitleEvent, mDescriptionEvent, mAddressEvent, mDayOfWeek, mDayOfMonth, mYear,
    mMonth;
    private SportEvent sportEvent;
    private NavigationMenu sportEventType;
    private int position;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConsultSportEvent.
     */
    public static ConsultSportEvent newInstance(int position,
                                                NavigationMenu sportEventType) {
        ConsultSportEvent fragment = new ConsultSportEvent();
        fragment.position = position;
        fragment.sportEventType = sportEventType;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_POSITION_TAG, position);
        outState.putString(CURRENT_SPORT_TYPE_TAG, getString(sportEventType.getNameID()));
        super.onSaveInstanceState(outState);
    }

    public ConsultSportEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadSaveInstance(savedInstanceState);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consult_sport_event, container, false);
        mTitleEvent = (TextView) view.findViewById(R.id.sport_event_name);
        mDescriptionEvent = (TextView) view.findViewById(R.id.sport_event_description);
        mAddressEvent = (TextView) view.findViewById(R.id.sport_event_address);
        mDayOfWeek = (TextView) view.findViewById(R.id.day_of_week_event);
        mDayOfMonth = (TextView) view.findViewById(R.id.day_of_month_event);
        mMonth = (TextView) view.findViewById(R.id.month_event);
        mYear = (TextView) view.findViewById(R.id.year_event);

        sportEvent = loadSportEvent();

        mTitleEvent.setText(sportEvent.getName());
        mDescriptionEvent.setText(sportEvent.getDescription());
        mAddressEvent.setText(sportEvent.getAddress());
        Calendar cal = USMAApplication.DateToCalendar(sportEvent.getDate());
        mYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        mMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        mDayOfMonth.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        mDayOfWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.LONG, Locale.getDefault()));
        setHeaderImage();
        ((MainActivity)getActivity()).hideFab(true);

        return view;
    }

    private void setHeaderImage() {
        if (sportEvent.getType(getResources()) == NavigationMenu.RACES) {
            SportsEventRaceType raceType = sportEvent.getSportEventRaceType(getResources());
            ((MainActivity) getActivity()).setHeaderImage(raceType.getImageID());
        }
    }
    private void loadSaveInstance(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            position = savedInstanceState.getInt(CURRENT_POSITION_TAG);
            if (savedInstanceState.getString(CURRENT_SPORT_TYPE_TAG).
                    equals(getString(NavigationMenu.RACES.getNameID()))) {
                sportEventType = NavigationMenu.RACES;
            } else {
                sportEventType = NavigationMenu.TRAINING;
            }
        }
    }

    private SportEvent loadSportEvent() {
        return ((MainActivity) getActivity()).getSportEvent(sportEventType).get(position);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        //show plus button
        ((MainActivity)getActivity()).hideFab(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_cancel:
                closeConsultSport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeConsultSport() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.beginTransaction().remove(this).commit();
        ((MainActivity)getActivity()).selectItem(sportEventType.getId() + 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.new_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
