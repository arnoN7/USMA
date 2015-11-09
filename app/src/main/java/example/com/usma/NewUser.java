package example.com.usma;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUser extends Fragment {

    private EditText inputName, inputEmail, inputFirstname, inputLicence;
    private TextInputLayout inputLayoutName, inputLayoutFirstname, inputLayoutEmail,
            inputLayoutLicence, inputLayoutBirthdate;
    private Button saveButton;

    private OnFragmentInteractionListener mListener;
    private DatePickerDialog datePickerDialog;
    private EditText inputBirthdate;
    private Date birthDate;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE);
    private ParseUser currentUser;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewUser.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUser newInstance() {
        NewUser fragment = new NewUser();

        return fragment;
    }

    public NewUser() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).hideFab(false);
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
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutFirstname = (TextInputLayout) view.findViewById(R.id.input_layout_firstname);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutLicence = (TextInputLayout) view.findViewById(R.id.input_layout_licence);
        inputLayoutBirthdate = (TextInputLayout) view.findViewById(R.id.input_layout_birth_date);

        inputName = (EditText) view.findViewById(R.id.input_name);
        inputFirstname = (EditText) view.findViewById(R.id.input_firstname);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputLicence = (EditText) view.findViewById(R.id.input_licence);
        inputBirthdate = (EditText) view.findViewById(R.id.input_birth_date);
        ((MainActivity)getActivity()).hideFab(true);
        ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                setTitle(getString(R.string.new_user));

        saveButton = (Button) view.findViewById(R.id.save_user);
        setDateFields();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });


        return view;

    }
    private void setDateFields(){
        inputBirthdate.setOnClickListener(new View.OnClickListener() {
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
                birthDate = newDate.getTime();
                inputBirthdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validateFirstname()) {
            return;
        }
        if (!validateLicence()) {
            return;
        }

        if (!validateBirthdate()) {
            return;
        }
        final ParseUser newUser = new ParseUser();
        currentUser = ParseUser.getCurrentUser();
        newUser.setUsername(inputEmail.getText().toString());
        newUser.setEmail(inputEmail.getText().toString());
        newUser.put(User.NAME, inputName.getText().toString());
        newUser.put(User.FIRSTNAME, inputFirstname.getText().toString());
        newUser.put(User.BIRTHDATE, birthDate);
        newUser.setPassword(inputLicence.getText().toString());
        newUser.put(User.LICENCE, inputLicence.getText().toString());
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                ParseUser.logInInBackground(currentUser.getUsername(),
                        currentUser.get(User.LICENCE).toString());
                Toast.makeText(getActivity().getApplicationContext(),
                        newUser.getString(User.FIRSTNAME) + " " +
                                newUser.getString(User.NAME) + " " + getString(R.string.added),
                        Toast.LENGTH_SHORT).show();

            }
        });
        ((ListFragmentUsers)((MainActivity)getActivity()).
                getLoadedRootFragment(NavigationMenu.USERS)).notifyDataSetChanged();
        closeNewUser();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            USMAApplication.requestFocus(inputName, getActivity());
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateBirthdate() {
        if (inputBirthdate.getText().toString().trim().isEmpty()) {
            inputLayoutBirthdate.setError(getString(R.string.err_msg_birthdate));
            USMAApplication.requestFocus(inputBirthdate, getActivity());
            return false;
        } else {
            inputLayoutBirthdate.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFirstname() {
        if (inputFirstname.getText().toString().trim().isEmpty()) {
            inputLayoutFirstname.setError(getString(R.string.err_msg_firstname));
            USMAApplication.requestFocus(inputFirstname, getActivity());
            return false;
        } else {
            inputLayoutFirstname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLicence() {
        if (inputLicence.getText().toString().trim().isEmpty()) {
            inputLayoutLicence.setError(getString(R.string.err_msg_licence));
            USMAApplication.requestFocus(inputLicence, getActivity());
            return false;
        } else {
            inputLayoutLicence.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            USMAApplication.requestFocus(inputEmail, getActivity());
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.new_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        //// TODO: 02/11/2015  
        super.onDetach();
        mListener = null;
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
                closeNewUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void closeNewUser() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.beginTransaction().remove(this).commit();
        ((MainActivity)getActivity()).selectItem(NavigationMenu.USERS.getId() + 1);
    }

}
