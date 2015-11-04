package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;

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

    @Override
    protected List<Item> getItems() {
        users = ((MainActivity) getActivity()).getUsers();
        List<Item> items = new ArrayList<>();
        if (users != null) {
            for (ParseUser user : users
                    ) {
                items.add(new Item(NavigationMenu.USERS, user.get(User.FIRSTNAME) + " " +
                        user.get(User.NAME), user.getEmail()));
            }
        }
        return items;
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
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("tag");
        ft.commit();
    }

    public void addUser(ParseUser newUser) {
        users.add(newUser);
        super.addItem(new Item(NavigationMenu.USERS, newUser.get(User.FIRSTNAME) + " " +
                newUser.get(User.NAME), newUser.getEmail()));
    }



}
