package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

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
    protected List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        groups = ((MainActivity)getActivity()).getGroups();
        if(groups != null){
            for (ParseRole group : groups
                    ) {
                items.add(new Item(NavigationMenu.GROUPS, group.getString(GroupUsers.NAME),
                        group.getString(GroupUsers.DESCRIPTION)));
            }
        }
        return items;
    }

    @Override
    public void newItemAction() {
        //update the main content by replacing fragments
        final Fragment fragment = new NewGroup();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("tag");
        ft.commit();
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
    public void addGroup(ParseRole newGroup) {
        groups.add(newGroup);
        super.addItem(new Item(NavigationMenu.GROUPS, newGroup.getName(),
                newGroup.getString(GroupUsers.DESCRIPTION)));
    }
}
