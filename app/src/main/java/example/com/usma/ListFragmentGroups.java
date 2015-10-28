package example.com.usma;

import android.os.Bundle;

import com.parse.ParseRole;

import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentGroups extends ListFragment {
    private List<ParseRole> groups;

    public ListFragmentGroups () {

    }

    public static ListFragmentGroups newInstance() {
        ListFragmentGroups fragment = new ListFragmentGroups();
        return fragment;
    }

    public void setGroups(List<ParseRole> groups) {
        this.groups = groups;
    }
}
