package example.com.usma;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTraining.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTraining#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTraining extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextInputLayout inputLayoutTrainingName, inputLayoutTrainingDate;
    private EditText inputTrainingName, inputTrainingDescription, inputTrainingDate,
            inputTrainingAddress;
    private Spinner inputSportType;
    private Button saveButton;
    private String[] sportEventTypes;
    private DatePickerDialog datePickerDialog;
    private Date trainingDate;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE);
    private String oldTitle;
    private NavigationMenu sportEventType;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewTraining.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTraining newInstance(NavigationMenu sportEventType) {
        NewTraining fragment = new NewTraining();
        fragment.sportEventType = sportEventType;

        return fragment;
    }

    public NewTraining() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_new_training, container, false);
        inputSportType = (Spinner) view.findViewById(R.id.sports_type);
        sportEventTypes = getResources().getStringArray(R.array.array_training_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_training_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputSportType.setAdapter(adapter);
        inputLayoutTrainingName = (TextInputLayout)
                view.findViewById(R.id.input_layout_training_name);
        inputLayoutTrainingDate = (TextInputLayout) view.findViewById(R.id.input_layout_training_date);
        inputTrainingName = (EditText) view.findViewById(R.id.input_training_name);
        inputTrainingDescription = (EditText) view.findViewById(R.id.input_training_description);
        inputTrainingDate = (EditText) view.findViewById(R.id.input_training_date);
        inputTrainingAddress = (EditText) view.findViewById(R.id.input_training_address);

        saveButton = (Button) view.findViewById(R.id.save_race);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        oldTitle = ((MainActivity)getActivity()).getCollapsingToolbarLayout().getTitle().toString();
        ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                setTitle(getString(R.string.new_training));
        trainingDate = null;
        setDateFields();
        ((MainActivity)getActivity()).hideFab(true);
        return view;

    }

    private void submitForm() {
        if (!validateTrainingName()) {
            return;
        }

        if (!validateTrainingDate()) {
            return;
        }

        final SportEvent newTraining = new SportEvent();
        newTraining.setName(inputTrainingName.getText().toString());
        newTraining.setDescription(inputTrainingDescription.getText().toString());
        newTraining.setSportType(sportEventTypes[inputSportType.getSelectedItemPosition()]);
        newTraining.setType(getString(sportEventType.getNameID()));
        newTraining.setAddress(inputTrainingAddress.getText().toString());
        newTraining.setDate(trainingDate);
        newTraining.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                closeNewTraining();
                Toast.makeText(getActivity().getApplicationContext(),
                        newTraining.getName() + " " + getString(R.string.added),
                        Toast.LENGTH_SHORT).show();
                ((ListFragmentSportEvent) ((MainActivity) getActivity()).getCurrentFragment()).
                        addSportEvent(newTraining, NavigationMenu.TRAINING);
            }
        });

    }
    private void setDateFields(){
        inputTrainingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
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
        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack();
        ((MainActivity)getActivity()).getCollapsingToolbarLayout().setTitle(oldTitle);
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

}
