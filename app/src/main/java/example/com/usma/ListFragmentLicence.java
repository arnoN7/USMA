package example.com.usma;

import android.os.Bundle;

import java.util.List;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentLicence extends ListFragment {

    public static ListFragmentLicence newInstance() {
        ListFragmentLicence fragment = new ListFragmentLicence();
        return fragment;
    }

    @Override
    protected List<Item> getItems() {
        return null;
    }

    @Override
    public void newItemAction() {
        //TODO
    }
}
