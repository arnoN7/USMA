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
    public void newItem() {
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

    public void setGroups(List<ParseRole> groups) {
        this.groups = groups;
        List<Item> items = new ArrayList<>();
        for (ParseRole group:groups
                ) {
            items.add(new Item(NavigationMenu.GROUPS, group.getString(GroupUsers.NAME),
                    group.getString(GroupUsers.DESCRIPTION)));
        }
        super.setItems(items);
    }
}
