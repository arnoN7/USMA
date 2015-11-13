package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnFragmentInteractionListener,
        NewUser.OnFragmentInteractionListener,
        ConsultSportEvent.OnFragmentInteractionListener {
    public static final String LIST_FRAGMENT_USER = "ListFragmentUsers";
    public static final String LIST_FRAGMENT_GROUPS = "ListFragmentGroups";
    public static final String LIST_FRAGMENT_RACE = "ListFragmentRace";
    public static final String LIST_FRAGMENT_TRAINING = "ListFragmentTraining";
    public static final String LIST_FRAGMENT_LICENCE = "ListFragmentLicence";
    public static final String LIST_FRAGMENT_NEW_USER = "ListFragmentNewUser";
    public static final String LIST_FRAGMENT_NEW_GROUP = "ListFragmentNewGroup";
    public static final String LIST_FRAGMENT_NEW_TRAINING = "ListFragmentNewTraining";
    public static final String LIST_FRAGMENT_NEW_RACE = "ListFragmentNewTraining";

    public static final String LIST_FRAGMENT_CONSULT_USER = "ListFragmentConsultUser";
    public static final String LIST_FRAGMENT_CONSULT_GROUP = "ListFragmentConsultGroup";
    public static final String LIST_FRAGMENT_CONSULT_TRAINING = "ListFragmentConsultTraining";
    public static final String LIST_FRAGMENT_CONSULT_RACE = "ListFragmentConsultTraining";

    private static final String LIST_FRAGMENT_NO_TAG = "noTag";

    private static final String CURRENT_FRAGMENT_TAG = "currentFragmentTag";
    private static final String CURRENT_TITLE_TAG = "currentTitleTag";
    private static final String CURRENT_HEADER_IMAGE_ID_TAG = "currentHeaderImageTag";
    public static final String PARSE_PIN_GROUP_TAG = "GROUPS";
    public static final String PARSE_PIN_USER_TAG = "USERS";
    public static final String PARSE_PIN_USERS_IN_GROUPS = "UsersInGroups";
    public static final String PARSE_PIN_RACE_TAG = "RACE";

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    public NavigationMenu[] navigationMenu = {NavigationMenu.RACES, NavigationMenu.TRAINING,
            NavigationMenu.GROUPS, NavigationMenu.LICENCE, NavigationMenu.USERS};
    private List<Fragment> fragments;
    private String currentFragmentTag;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView headerImage;
    private int headerImageID;
    private ParseRole adminRole;


    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String name;
    String email;
    int PROFILE = R.drawable.logousma;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    private Toolbar toolbar;

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    FloatingActionButton buttonNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((savedInstanceState != null) &&
                savedInstanceState.containsKey(CURRENT_FRAGMENT_TAG)){
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG);
            headerImageID = savedInstanceState.getInt(CURRENT_HEADER_IMAGE_ID_TAG);

        } else {
            currentFragmentTag = LIST_FRAGMENT_NO_TAG;
            headerImageID = R.drawable.athle;
        }

        adminRole = null;
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        name = ParseUser.getCurrentUser().getString(User.FIRSTNAME) + " "
                + ParseUser.getCurrentUser().getString(User.NAME);
        email = ParseUser.getCurrentUser().getEmail();
        buttonNew = (FloatingActionButton) findViewById(R.id.button_new);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        headerImage = (ImageView) findViewById(R.id.toolbarImage);
        setHeaderImage(headerImageID);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new NavigationDrawerAdapter(navigationMenu,name,email,PROFILE, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        if(currentFragmentTag != LIST_FRAGMENT_NO_TAG) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, currentFragment, currentFragmentTag);
            ft.commit();
            String title = savedInstanceState.getString(CURRENT_TITLE_TAG);
            collapsingToolbarLayout.setTitle(title);
            initFragmentsFromLocalDataStore();
        } else {
            initOnlineIfPossible();
            selectItem(NavigationMenu.RACES.getId()+1);
        }
        initNewButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void setHeaderImage (int imageID) {
        headerImageID = imageID;
        headerImage.setImageResource(imageID);
    }

    public DrawerLayout getDrawer() {
        return Drawer;
    }

    public void selectItem(int position) {
        // update the main content by replacing fragments
        if(position != 0 || position <= fragments.size()) {
            if(NavigationMenu.RACES.getId() + 1 == position) {
                setCurrentFragmentTag(LIST_FRAGMENT_RACE);
            } else if (NavigationMenu.TRAINING.getId() + 1 == position) {
                setCurrentFragmentTag(LIST_FRAGMENT_TRAINING);
            } else if (NavigationMenu.GROUPS.getId() + 1 == position) {
                setCurrentFragmentTag(LIST_FRAGMENT_GROUPS);
            } else if (NavigationMenu.LICENCE.getId() + 1 == position) {
                setCurrentFragmentTag(LIST_FRAGMENT_LICENCE);
            } else if (NavigationMenu.USERS.getId() + 1 == position) {
                setCurrentFragmentTag(LIST_FRAGMENT_USER);
            }

            FragmentManager fragmentManager = getFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
            if(currentFragment == null) {
                //If selected fragment was never used retreived loaded fragment
                currentFragment = fragments.get(position - 1);
            }

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, currentFragment, currentFragmentTag);
            ft.commit();
            initNewButton();

        }
        // update selected item title, then close the drawer
        collapsingToolbarLayout.setTitle(getString(navigationMenu[position-1].getNameID()));
        getDrawer().closeDrawers();
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setUsers(final List<ParseUser> users) {
        try {
            ParseUser.unpinAll(PARSE_PIN_USER_TAG);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseUser.pinAllInBackground(PARSE_PIN_USER_TAG, users);
        if(currentFragmentTag.equals(LIST_FRAGMENT_USER)) {
            ((ListFragment) fragments.get(NavigationMenu.USERS.getId())).notifyDataSetChanged();
        }
    }

    public void setGroups(final List<ParseRole> groups) {
        try {
            ParseRole.unpinAll(PARSE_PIN_GROUP_TAG);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseRole.pinAllInBackground(PARSE_PIN_GROUP_TAG, groups);
        if (currentFragmentTag.equals(LIST_FRAGMENT_GROUPS)) {
            ((ListFragment) fragments.get(NavigationMenu.GROUPS.getId())).notifyDataSetChanged();
        }
    }

    public void setTrainings(final List<SportEvent> trainings, boolean online) {
        for (SportEvent sportEvent: trainings
             ) {
            sportEvent.getACL().setReadAccess(ParseUser.getCurrentUser(), true);
            pinSportEventGroups(online, sportEvent);
        }
        SportEvent.pinAllInBackground(trainings);
        if (currentFragmentTag.equals(LIST_FRAGMENT_TRAINING)) {
            ((ListFragment) fragments.get(NavigationMenu.TRAINING.getId())).notifyDataSetChanged();
        }
    }

    public void setRaces(final List<SportEvent> races, boolean online) {
        try {
            SportEvent.unpinAll(PARSE_PIN_RACE_TAG);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SportEvent.pinAllInBackground(PARSE_PIN_RACE_TAG, races);
        for (SportEvent sportEvent: races
                ) {
            sportEvent.getACL().setReadAccess(ParseUser.getCurrentUser(), true);
            pinSportEventGroups(online, sportEvent);

        }
        if (currentFragmentTag.equals(LIST_FRAGMENT_RACE)) {
            ((ListFragment) fragments.get(NavigationMenu.RACES.getId())).notifyDataSetChanged();
        }
    }

    private void pinSportEventGroups(boolean online, SportEvent sportEvent) {
        if(online) {
            sportEvent.getGroupsRelation().getQuery().findInBackground(new FindCallback<ParseRole>() {
                @Override
                public void done(List<ParseRole> groups, ParseException e) {
                    ParseRole.pinAllInBackground(groups);
                }
            });
        }
        else
        {
            ParseRole.pinAllInBackground(sportEvent.getGroups(false));
        }
    }

    private void initOnlineIfPossible() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
            initFragments();
        } else {
            // If there is no connection, let the user know the sync didn't happen
            initFragmentsFromLocalDataStore();
            Toast.makeText(
                    getApplicationContext(),
                    "Your device appears to be offline. Some todos may not have been synced to Parse.",
                    Toast.LENGTH_LONG).show();
        }
    }


    private void initFragmentsFromLocalDataStore() {
        fragments = new ArrayList<>();
        for (int i = 0; i < navigationMenu.length; i++) {
            switch (navigationMenu[i]) {
                case USERS:
                    ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
                    queryUsers.fromLocalDatastore();
                    try {
                        Fragment fragment = getLocalFragmentByTag(LIST_FRAGMENT_USER);
                        if(fragment == null) {
                            fragment = ListFragmentUsers.newInstance();
                        }
                        fragments.add(fragment);
                        setUsers(queryUsers.find());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case GROUPS:
                    ParseQuery<ParseRole> queryGroups = ParseRole.getQuery();
                    queryGroups.fromLocalDatastore();
                    try {
                        Fragment fragment = getLocalFragmentByTag(LIST_FRAGMENT_GROUPS);
                        if(fragment == null) {
                            fragment = ListFragmentGroups.newInstance();
                        }
                        fragments.add(fragment);
                        setGroups(queryGroups.find());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case RACES:
                case TRAINING:
                    ParseQuery<SportEvent> querySportEvent = ParseQuery.getQuery(SportEvent.class);
                    querySportEvent.whereEqualTo(SportEvent.MENU_TYPE,
                            getString(navigationMenu[i].getNameID()));
                    querySportEvent.orderByDescending(SportEvent.DATE);
                    querySportEvent.fromLocalDatastore();
                    try {
                        Fragment fragment = null;
                        if(navigationMenu[i] == NavigationMenu.RACES) {
                            fragment = getLocalFragmentByTag(LIST_FRAGMENT_RACE);
                            if(fragment == null) {
                                fragment = ListFragmentSportEvent.newInstance(navigationMenu[i]);
                            }
                            fragments.add(fragment);
                            setRaces(querySportEvent.find(), false);
                        } else {
                            fragment = getLocalFragmentByTag(LIST_FRAGMENT_TRAINING);
                            if(fragment == null) {
                                fragment = ListFragmentSportEvent.newInstance(navigationMenu[i]);
                            }
                            fragments.add(fragment);
                            setTrainings(querySportEvent.find(), false);
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case LICENCE:
                    ListFragmentLicence fragmentLicence = ListFragmentLicence.newInstance();
                    fragments.add(fragmentLicence);
                    break;

            }
        }
    }

    public void initFragments () {
        fragments = new ArrayList<>();
        try {
            ParseUser.unpinAll();
            ParseRole.unpinAll();
            SportEvent.unpinAll();
            CommentSportEvent.unpinAll();
            ParseUser.unpinAllInBackground(PARSE_PIN_USERS_IN_GROUPS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < navigationMenu.length; i++) {
            switch (navigationMenu[i]) {
                case USERS:
                    ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
                    queryUsers.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                Toast debug = Toast.makeText(getApplication(), "Users Loaded", Toast.LENGTH_LONG);
                                debug.show();
                                setUsers(objects);
                            } else {

                            }
                        }
                    });
                    ListFragmentUsers fragment = ListFragmentUsers.newInstance();
                    fragments.add(fragment);

                    break;
                case GROUPS:
                    ParseQuery<ParseRole> queryGroups = ParseRole.getQuery();
                    queryGroups.findInBackground(new FindCallback<ParseRole>() {
                        @Override
                        public void done(List<ParseRole> listRole, ParseException e) {
                            adminRole = ((ListFragmentGroups) fragments.get(NavigationMenu.GROUPS.
                                    getId())).getAdminRole();
                            try {
                                ParseUser.unpinAll(PARSE_PIN_USERS_IN_GROUPS);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            for (ParseRole group : listRole
                                    ) {
                                group.getUsers().getQuery().findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> users, ParseException e) {
                                        //Pin all users attached to a group
                                        ParseUser.pinAllInBackground(PARSE_PIN_USERS_IN_GROUPS, users);
                                    }
                                });
                            }
                            setGroups(listRole);
                            Toast debug = Toast.makeText(getApplication(), "Groups Loaded",
                                    Toast.LENGTH_LONG);
                            debug.show();
                        }
                    });
                    ListFragmentGroups fragmentGroups = ListFragmentGroups.newInstance();
                    fragments.add(fragmentGroups);

                    break;
                case RACES:
                case TRAINING:
                    ParseQuery<SportEvent> querySportEvent = ParseQuery.getQuery(SportEvent.class);
                    querySportEvent.whereEqualTo(SportEvent.MENU_TYPE,
                            getString(navigationMenu[i].getNameID()));
                    querySportEvent.orderByDescending(SportEvent.DATE);
                    querySportEvent.findInBackground(new FindCallback<SportEvent>() {
                        @Override
                        public void done(List<SportEvent> sportEvents, ParseException e) {
                            NavigationMenu sportEventType;
                            if ((sportEvents != null) && (sportEvents.size() > 0)) {
                                sportEventType = sportEvents.get(0).getType(getResources());
                                if (sportEventType == NavigationMenu.RACES) {
                                    setRaces(sportEvents, true);
                                } else if (sportEventType == NavigationMenu.TRAINING) {
                                    setTrainings(sportEvents, true);
                                }
                                Toast debug = Toast.makeText(getApplication(),
                                        getString(sportEventType.getNameID()) + " loaded",
                                        Toast.LENGTH_LONG);
                                debug.show();
                            }
                        }
                    });
                    ListFragmentSportEvent fragmentSportEvent = ListFragmentSportEvent.
                            newInstance(navigationMenu[i]);
                    fragments.add(fragmentSportEvent);
                    break;

                case LICENCE:
                    ListFragmentLicence fragmentLicence = ListFragmentLicence.newInstance();
                    fragments.add(fragmentLicence);
                    break;

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_FRAGMENT_TAG, currentFragmentTag);
        outState.putString(CURRENT_TITLE_TAG, collapsingToolbarLayout.getTitle().toString());
        outState.putInt(CURRENT_HEADER_IMAGE_ID_TAG, headerImageID);
        super.onSaveInstanceState(outState);
    }

    public Fragment getCurrentFragment() {
        return getLocalFragmentByTag(currentFragmentTag);
    }

    public Fragment getLocalFragmentByTag(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager.findFragmentByTag(tag);
    }

    public Fragment getLoadedRootFragment(NavigationMenu menu) {
        return fragments.get(menu.getId());
    }

    public void setCurrentFragmentTag(String currentFragmentTag) {
        this.currentFragmentTag = currentFragmentTag;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void logout() {
        ParseUser.logOut();
        Intent goToLoginActivity = new Intent(getApplication(), LoginActivity.class);
        startActivity(goToLoginActivity);
    }

    public void hideFab(boolean hide) {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)    buttonNew.getLayoutParams();
        if (hide) {
            buttonNew.setVisibility(View.GONE);
        }
        else {
            buttonNew.setVisibility(View.VISIBLE);
        }


    }


    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    /**
     * Get Users from local datastore
     * @return
     */
    public List<ParseUser> getUsers() {
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        List<ParseUser> users = null;
        try {
            users = queryUsers.fromLocalDatastore().find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Get Roles from local datastore
     * @return
     */
    public List<ParseRole> getGroups(boolean refresh) {
        ParseQuery<ParseRole> queryGroups = ParseRole.getQuery();
        List<ParseRole> groups = null;
        try {
            if(!refresh) {
                groups = queryGroups.fromLocalDatastore().find();
            } else {
                groups = queryGroups.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return groups;
    }

    /**
     * Get Sport Events from local datastore
     * @return
     */
    public List<SportEvent> getSportEvent(NavigationMenu sportType) {
        ParseQuery<SportEvent> querySportEvent = ParseQuery.getQuery(SportEvent.class);
        querySportEvent.whereEqualTo(SportEvent.MENU_TYPE,
                getString(sportType.getNameID()));
        querySportEvent.orderByAscending(SportEvent.DATE);
        querySportEvent.fromLocalDatastore();
        List<SportEvent> sportEvents = null;
        try {
            sportEvents = querySportEvent.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sportEvents;
    }

    public void initNewButton() {
        if(currentFragmentTag == LIST_FRAGMENT_RACE ||
                currentFragmentTag == LIST_FRAGMENT_TRAINING ||
                currentFragmentTag == LIST_FRAGMENT_USER ||
                currentFragmentTag == LIST_FRAGMENT_GROUPS) {
            hideFab(false);
            Fragment currentFragment = getCurrentFragment();
            buttonNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListFragment)getCurrentFragment()).newItemAction();
                }
            });

        } else {
            hideFab(true);
        }
    }

    public String getCurrentFragmentTag() {
        return currentFragmentTag;
    }
}
