package example.com.usma;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewSportEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewSportEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewSportEvent extends Fragment implements FragmentSpecialClosing{

    private OnFragmentInteractionListener mListener;
    private TextInputLayout inputLayoutTrainingName, inputLayoutTrainingAddress,
            inputLayoutTrainingDescription, inputLayoutTrainingDate;
    private EditText inputTrainingName, inputTrainingDescription, inputTrainingDate,
            inputTrainingAddress;
    private TextView textSportEventTrainingType;
    private Spinner inputSportType;
    private Button saveButton;
    private String[] sportEventTypes;
    private DatePickerDialog datePickerDialog;
    private Date trainingDate;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE);
    private NavigationMenu navigationMenu;
    private SportEvent currentSportEvent;
    private int currentPosition = 0;
    private ListView mGroups;
    private List<Integer> selectedGroupId;
    private ListGroupAdapter groupAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewTraining.
     */
    // TODO: Rename and change types and number of parameters
    public static NewSportEvent newInstance(NavigationMenu sportEventType) {
        NewSportEvent fragment = new NewSportEvent();
        fragment.navigationMenu = sportEventType;

        return fragment;
    }

    public NewSportEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(currentSportEvent != null) {
            outState.putInt(ListFragmentSportEvent.MODIFY_EVENT_TAG, currentPosition);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        currentSportEvent = null;
        selectedGroupId = new ArrayList<>();
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(ListFragmentSportEvent.MODIFY_EVENT_TAG)) {
            currentPosition = savedInstanceState.getInt(ListFragmentSportEvent.MODIFY_EVENT_TAG);
            currentSportEvent = ((MainActivity)getActivity()).
                    getSportEvent(navigationMenu).get(currentPosition);
        }
        final View view =inflater.inflate(R.layout.fragment_sport_event, container, false);
        int arraySportEventTypesID;
        inputSportType = (Spinner) view.findViewById(R.id.sports_type);
        if (navigationMenu == NavigationMenu.RACES) {
            arraySportEventTypesID = R.array.array_race_type;
        } else {
            arraySportEventTypesID = R.array.array_training_type;
        }
        sportEventTypes = getResources().getStringArray(arraySportEventTypesID);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                arraySportEventTypesID, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputSportType.setAdapter(adapter);
        inputLayoutTrainingName = (TextInputLayout)
                view.findViewById(R.id.input_layout_training_name);
        inputLayoutTrainingDate = (TextInputLayout) view.findViewById(R.id.
                input_layout_training_date);
        inputLayoutTrainingAddress = (TextInputLayout) view.findViewById(R.
                id.input_layout_training_details);
        inputLayoutTrainingDescription = (TextInputLayout) view.
                findViewById(R.id.input_layout_training_description);
        inputTrainingName = (EditText) view.findViewById(R.id.input_training_name);
        inputTrainingDescription = (EditText) view.findViewById(R.id.input_training_description);
        inputTrainingDate = (EditText) view.findViewById(R.id.input_training_date);
        inputTrainingAddress = (EditText) view.findViewById(R.id.input_training_address);
        textSportEventTrainingType = (TextView) view.findViewById(R.id.text_sport_event_type);
        mGroups = (ListView) view.findViewById(R.id.RecyclerViewNewSportEvent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        List<ParseRole> groups = ((MainActivity)getActivity()).getGroups(false);
        groupAdapter = new ListGroupAdapter(getActivity(), groups);
        mGroups.setAdapter(groupAdapter);
        //getHeight to resize the listView...
        ViewGroup.LayoutParams params = mGroups.getLayoutParams();
        params.height = groupAdapter.getHeight(mGroups);
        mGroups.setLayoutParams(params);

        saveButton = (Button) view.findViewById(R.id.save_race);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                submitForm();
            }
        });
        ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                setTitle(getString(R.string.new_training));
        ((MainActivity)getActivity()).hideFab(true);

        if (navigationMenu == NavigationMenu.RACES) {
            inputLayoutTrainingName.setHint(getString(R.string.hint_race_name));
            inputLayoutTrainingDate.setHint(getString(R.string.hint_race_date));
            inputLayoutTrainingDescription.setHint(getString(R.string.hint_race_overview));
            inputLayoutTrainingAddress.setHint(getString(R.string.hint_race_details));
            textSportEventTrainingType.setText(getString(R.string.race_type));
            saveButton.setText(getString(R.string.save_race));
            ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                    setTitle(getString(R.string.new_race));
        } else {
            inputLayoutTrainingName.setHint(getString(R.string.hint_training_name));
            inputLayoutTrainingDate.setHint(getString(R.string.hint_training_date));
            inputLayoutTrainingDescription.setHint(getString(R.string.hint_training_overview));
            inputLayoutTrainingAddress.setHint(getString(R.string.hint_training_details));
            textSportEventTrainingType.setText(getString(R.string.training_type));
            saveButton.setText(getString(R.string.save_training));
            ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                    setTitle(getString(R.string.new_training));
        }
        if(args != null) {
            currentPosition = getArguments().getInt(ListFragmentSportEvent.MODIFY_EVENT_TAG);
            currentSportEvent = ((MainActivity)getActivity()).
                    getSportEvent(navigationMenu).get(currentPosition);
            navigationMenu = currentSportEvent.getType(getResources());
            SportsEventType sportsEventType = currentSportEvent.getSportEventType(getResources(),
                    navigationMenu);
            inputTrainingName.setText(currentSportEvent.getName());
            inputTrainingDescription.setText(currentSportEvent.getDescription());
            inputTrainingDate.setText(dateFormatter.format(currentSportEvent.getDate()));
            inputTrainingAddress.setText(currentSportEvent.getAddress());
            inputSportType.setSelection(sportsEventType.getPosition());
            groupAdapter.setChecked(currentSportEvent.getGroups(false));
        }
        setDateFields();
        return view;

    }

    private void submitForm() {
        if (!validateTrainingName()) {
            return;
        }

        if (!validateTrainingDate()) {
            return;
        }

        if(currentSportEvent == null) {
            currentSportEvent = new SportEvent();
        }
        currentSportEvent.setName(inputTrainingName.getText().toString());
        currentSportEvent.setDescription(inputTrainingDescription.getText().toString());
        currentSportEvent.setSportType(sportEventTypes[inputSportType.getSelectedItemPosition()]);
        currentSportEvent.setType(getString(navigationMenu.getNameID()));
        currentSportEvent.setAddress(inputTrainingAddress.getText().toString());
        currentSportEvent.setDate(trainingDate);
        setSportEventGroups();
        try {
            //public read access need to be True to read it in localdatastore
            currentSportEvent.getACL().setReadAccess(ParseUser.getCurrentUser(),true);
            currentSportEvent.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    String message;
                    if(currentSportEvent.getType(getActivity().getResources())
                            == NavigationMenu.RACES) {
                        message = getActivity().getString(R.string.start_push_race) + " ";
                    } else {
                        message = getActivity().getString(R.string.start_push_training) + " ";
                    }
                    message += currentSportEvent.getName();
                    GroupUsers.sendPUSHToGroup(currentSportEvent.getGroups(false), message);
                }
            });
            currentSportEvent.pin();
            //set false for cloud
            //currentSportEvent.getACL().setPublicReadAccess(false);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        closeNewTraining();

    }

    private void setSportEventGroups() {
        SparseBooleanArray checkedGroups = groupAdapter.getmCheckStates();
        List<ParseRole> groups = ((MainActivity)getActivity()).getGroups(false);
        ParseACL acl = new ParseACL();
        for (int i = 0; i < groups.size(); i++) {
            if(checkedGroups.get(i) == true) {
                acl.setRoleWriteAccess(groups.get(i), true);
                acl.setRoleReadAccess(groups.get(i), true);
                acl.setPublicReadAccess(false);
                acl.setPublicWriteAccess(false);
                currentSportEvent.addGroup(groups.get(i));
            }
        }
        currentSportEvent.setACL(acl);
    }

    private void setDateFields(){
        inputTrainingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        Calendar calendar = null;
        if(currentSportEvent != null ) {
            calendar = USMAApplication.DateToCalendar(currentSportEvent.getDate());
        }else {
            calendar = Calendar.getInstance();
        }
        trainingDate = calendar.getTime();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                trainingDate = newDate.getTime();
                inputTrainingDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean validateTrainingName() {
        if (inputTrainingName.getText().toString().trim().isEmpty()) {
            inputLayoutTrainingName.setError(getString(R.string.err_msg_training_name));
            USMAApplication.requestFocus(inputLayoutTrainingName, getActivity());
            return false;
        } else {
            inputLayoutTrainingName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTrainingDate() {
        if (inputTrainingDate.getText().toString().trim().isEmpty()) {
            inputLayoutTrainingDate.setError(getString(R.string.err_msg_training_date));
            USMAApplication.requestFocus(inputLayoutTrainingDate, getActivity());
            return false;
        } else {
            inputLayoutTrainingDate.setErrorEnabled(false);
        }

        return true;
    }

    private void closeNewTraining() {
        //FragmentManager fm = getActivity().getFragmentManager();
        //fm.beginTransaction().remove(this).commit();
        ((MainActivity)getActivity()).selectItem(navigationMenu.getId() + 1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).hideFab(false);
    }

    @Override
    public void onAttach(Context activity) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.new_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_cancel:
                closeNewTraining();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void specialClose() {
        closeNewTraining();
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

    public void checkGroup(int id) {
        selectedGroupId.add(id);
    }

    public void uncheckGroup(int id) {
        for (int i = 0; i < selectedGroupId.size(); i++) {
            if(selectedGroupId.get(i) == id) {
                selectedGroupId.remove(i);
            }
        }
    }

}
