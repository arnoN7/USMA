package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentUsers  extends ListFragment{

    private List<ParseUser> users;

    public ListFragmentUsers() {

    }

    public static ListFragmentUsers newInstance() {
        ListFragmentUsers fragment = new ListFragmentUsers();
        return fragment;
    }

    @Override
    public void newItemAction() {
        // update the main content by replacing fragments
        final Fragment fragment = new NewUser();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_NEW_USER);
        ft.addToBackStack(MainActivity.LIST_FRAGMENT_NEW_USER);
        ((MainActivity)getActivity()).setCurrentFragmentTag(MainActivity.LIST_FRAGMENT_NEW_USER);
        ft.commit();
    }

    @Override
    public void consultItemAction(int position) {
        //TODO
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ListFragmentUsers)((MainActivity)getActivity()).
                getLoadedRootFragment(NavigationMenu.USERS)).notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mAdapter = new ListItemAdapter.ListItemAdapterUser(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void specialClose() {
        //Nothing its a root fragment
    }
}
