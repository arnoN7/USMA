package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.parse.ParseRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentGroups extends ListFragment {
    private List<ParseRole> groups;


    public ListFragmentGroups () {

    }

    @Override
    public void newItemAction() {
        //update the main content by replacing fragments
        final Fragment fragment = new NewGroup();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, MainActivity.LIST_FRAGMENT_NEW_GROUP);
        ft.addToBackStack(MainActivity.LIST_FRAGMENT_NEW_GROUP);
        ((MainActivity)getActivity()).setCurrentFragmentTag(MainActivity.LIST_FRAGMENT_NEW_GROUP);
        ft.commit();
    }

    @Override
    public void consultItemAction(int position) {
        //Groups are not consultable all can be seen in the list
    }

    public static ListFragmentGroups newInstance() {
        ListFragmentGroups fragment = new ListFragmentGroups();
        return fragment;
    }

    public ParseRole getAdminRole() {
        ParseRole adminRole = null;
        if(groups != null) {
            for (ParseRole role : groups) {
                if (role.getName() == GroupUsers.ADMIN) {
                    return adminRole;
                }
            }
        }
        return adminRole;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ListFragmentGroups) ((MainActivity) getActivity()).
                getLoadedRootFragment(NavigationMenu.GROUPS)).notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if(mRecyclerView != null) {
            mAdapter = new ListItemAdapter.ListItemAdapterGroup(getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void specialClose() {
        //Nothing its a root fragment
    }
}
