package example.com.usma;

import android.os.Bundle;

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

    public void setUsers(List<ParseUser> users) {
        this.users = users;
        List<Item> items = new ArrayList<>();
        for (ParseUser user:users
             ) {
            items.add(new Item(NavigationMenu.USERS, user.get(User.FIRSTNAME) + " " + user.get(User.NAME), user.getEmail()));
        }
        super.setItems(items);

    }


}
