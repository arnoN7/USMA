package example.com.usma;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultSportEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultSportEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultSportEvent extends Fragment implements FragmentSpecialClosing {

    private static final String CURRENT_POSITION_TAG = "currentFragmentTag";
    private static final String CURRENT_SPORT_TYPE_TAG = "currentTitleTag";

    private OnFragmentInteractionListener mListener;

    private SportEvent sportEvent;
    private NavigationMenu navigationType;
    private RecyclerView mRecyclerViewComment;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private int position;
    private Context context;

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
        fragment.navigationType = sportEventType;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_POSITION_TAG, position);
        outState.putString(CURRENT_SPORT_TYPE_TAG, getString(navigationType.getNameID()));
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

        mRecyclerViewComment = (RecyclerView) view.findViewById(R.id.comment_layout);


        sportEvent = loadSportEvent();
        setHeaderImage();
        ((MainActivity)getActivity()).hideFab(true);
        mRecyclerViewComment.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL
               , false);
        mRecyclerViewComment.setLayoutManager(mLayoutManager);
        // specify an adapter with empty list of comment
        mAdapter = new ListCommentsAdapter(getActivity(), new ArrayList<CommentSportEvent>(),
                sportEvent, true);
        mRecyclerViewComment.setAdapter(mAdapter);
        //Load comments from the cloud
        loadComments(true);

        return view;
    }

    private void loadComments(boolean refresh) {
        if(mRecyclerViewComment != null) {
            ParseQuery<CommentSportEvent> query = ParseQuery.getQuery(CommentSportEvent.class);
            query.whereEqualTo(CommentSportEvent.SPORT_EVENT, sportEvent);
            query.orderByAscending(CommentSportEvent.DATE);
            if(!refresh) {
                query.fromLocalDatastore();
                try {
                    List<CommentSportEvent> comments = query.find();
                    refreshRecyclerView(comments, false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                query.findInBackground(new FindCallback<CommentSportEvent>() {
                    @Override
                    public void done(List<CommentSportEvent> comments, ParseException e) {
                        try {
                            if (comments != null) {
                                refreshRecyclerView(comments, false);
                            } else {
                                ((ListCommentsAdapter)mAdapter).showError(true);
                                Toast.makeText(getActivity().getApplication(), R.string.comment_cant_load,
                                        Toast.LENGTH_LONG);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
            }

        }
    }

    private void refreshRecyclerView(List<CommentSportEvent> comments, boolean isLoading)
            throws ParseException {
        CommentSportEvent.unpinAll();
        CommentSportEvent.pinAllInBackground(comments);
        mAdapter = new ListCommentsAdapter(getActivity(), comments, sportEvent, isLoading);
        mRecyclerViewComment.setAdapter(mAdapter);

        ViewGroup.LayoutParams params = mRecyclerViewComment.getLayoutParams();
    }

    private void setHeaderImage() {
        SportsEventType sportEventType = sportEvent.getSportEventType(getResources(),
                navigationType);
        ((MainActivity) getActivity()).setHeaderImage(sportEventType.getImageID());
    }
    private void loadSaveInstance(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            position = savedInstanceState.getInt(CURRENT_POSITION_TAG);
            if (savedInstanceState.getString(CURRENT_SPORT_TYPE_TAG).
                    equals(getString(NavigationMenu.RACES.getNameID()))) {
                navigationType = NavigationMenu.RACES;
            } else {
                navigationType = NavigationMenu.TRAINING;
            }
        }
    }

    private SportEvent loadSportEvent() {
        return ((MainActivity) getActivity()).getSportEvent(navigationType).get(position);
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

    @Override
    public void specialClose() {
        closeConsultSport();
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
        ((MainActivity)getActivity()).selectItem(navigationType.getId() + 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.new_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
