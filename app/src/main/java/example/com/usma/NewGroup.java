package example.com.usma;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseRole;
import com.parse.SaveCallback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewGroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGroup extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextInputLayout inputLayoutGroupName, inputLayoutGroupDescription;
    private EditText inputGroupName, inputGroupDescription;
    private Button saveButton;
    private ParseRole adminRole;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static NewGroup newInstance() {
        NewGroup fragment = new NewGroup();
        return fragment;
    }

    public NewGroup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
                closeNewGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_group, container, false);
        inputLayoutGroupName = (TextInputLayout) view.findViewById(R.id.input_layout_group_name);
        inputLayoutGroupDescription = (TextInputLayout) view.
                findViewById(R.id.input_layout_group_description);

        inputGroupName = (EditText) view.findViewById(R.id.input_group_name);
        inputGroupDescription = (EditText) view.findViewById(R.id.input_group_description);
        ((MainActivity)getActivity()).hideFab(true);
        ((MainActivity)getActivity()).getCollapsingToolbarLayout().
                setTitle(getString(R.string.new_group));

        saveButton = (Button) view.findViewById(R.id.save_group);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        return view;
    }

    private void submitForm() {
        if (!validateGroupName()) {
            return;
        }
        ParseACL roleACL = new ParseACL();
        roleACL.setPublicReadAccess(true);
        ParseRole newRole = new ParseRole(inputGroupName.getText().toString(),roleACL);
        newRole.put(GroupUsers.DESCRIPTION, inputGroupDescription.getText().toString());
        newRole.saveEventually();
        closeNewGroup();
    }

    private void closeNewGroup() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.beginTransaction().remove(this).commit();
        ((MainActivity)getActivity()).selectItem(NavigationMenu.GROUPS.getId() + 1);
    }

    private boolean validateGroupName() {
        if (inputGroupName.getText().toString().trim().isEmpty()) {
            inputLayoutGroupName.setError(getString(R.string.err_msg_name));
            USMAApplication.requestFocus(inputGroupName, getActivity());
            return false;
        } else {
            inputLayoutGroupName.setErrorEnabled(false);
        }

        return true;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
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

}
