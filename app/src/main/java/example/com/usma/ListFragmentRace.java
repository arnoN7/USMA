package example.com.usma;

import android.os.Bundle;

/**
 * Created by Arnaud Rover on 24/10/15.
 */
public class ListFragmentRace extends ListFragment {

    public static ListFragmentRace newInstance() {
        
        Bundle args = new Bundle();
        
        ListFragmentRace fragment = new ListFragmentRace();
        fragment.setArguments(args);
        return fragment;
    }
}
