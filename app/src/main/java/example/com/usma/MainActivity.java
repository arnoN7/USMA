package example.com.usma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
        NewUser.OnFragmentInteractionListener{
    public static final String LIST_FRAGMENT_USER = "ListFragmentUsers";
    private static final String LIST_FRAGMENT_GROUPS = "ListFragmentGroups";
    private static final String LIST_FRAGMENT_RACE = "ListFragmentRace";
    private static final String LIST_FRAGMENT_TRAINING = "ListFragmentTraining";
    private static final String LIST_FRAGMENT_LICENCE = "ListFragmentLicence";

    private static final String CURRENT_POSITION_DRAWER = "currentPositionDrawer";

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    public NavigationMenu[] navigationMenu = {NavigationMenu.RACES, NavigationMenu.TRAINING,
            NavigationMenu.GROUPS, NavigationMenu.LICENCE, NavigationMenu.USERS};
    private List<Fragment> fragments;
    private ListFragment currentFragment;
    private int currentPosition;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ParseRole adminRole;

    private List<ParseUser> users;
    private List<ParseRole> groups;
    private List<SportEvent> trainings;
    private List<SportEvent> races;

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
        if((savedInstanceState != null) && savedInstanceState.containsKey(CURRENT_POSITION_DRAWER)){
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION_DRAWER);
        } else {
            currentPosition = NavigationMenu.RACES.getId() +1;
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

        users = new ArrayList<>();
        groups = new ArrayList<>();
        trainings = new ArrayList<>();
        races = new ArrayList<>();

        initFragments();
        //+1 is added because of the header in the navigation drawer
        selectItem(currentPosition);

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

    public DrawerLayout getDrawer() {
        return Drawer;
    }

    public void selectItem(int position) {
        // update the main content by replacing fragments
        if(position != 0 || position <= fragments.size()) {
            String tag = null;
            currentPosition = position;
            if(NavigationMenu.RACES.getId() - 1 == position) {
                tag = LIST_FRAGMENT_RACE;
            } else if (NavigationMenu.TRAINING.getId() -1 == position) {
                tag = LIST_FRAGMENT_TRAINING;
            } else if (NavigationMenu.GROUPS.getId() - 1 == position) {
                tag = LIST_FRAGMENT_GROUPS;
            } else if (NavigationMenu.LICENCE.getId() -1 == position) {
                tag = LIST_FRAGMENT_LICENCE;
            } else if (NavigationMenu.USERS.getId() - 1 == position) {
                tag = LIST_FRAGMENT_USER;
            }

            FragmentManager fragmentManager = getFragmentManager();
            currentFragment = (ListFragment) fragmentManager.findFragmentByTag(tag);
            if(currentFragment == null) {
                //If selected fragment was never used retreived loaded fragment
                currentFragment = (ListFragment) fragments.get(position - 1);
            }


            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, currentFragment);
            ft.addToBackStack(tag);
            ft.commit();
            buttonNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFragment.newItemAction();
                }
            });
        }
        // update selected item title, then close the drawer
        collapsingToolbarLayout.setTitle(getString(navigationMenu[position-1].getNameID()));
        getDrawer().closeDrawers();
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setUsers(List<ParseUser> users) {
        this.users = users;
        if(fragments.get(NavigationMenu.USERS.getId()).equals(currentFragment)) {
            ((ListFragment) fragments.get(NavigationMenu.USERS.getId())).notifyDataSetChanged();
        }
    }

    public void setGroups(List<ParseRole> groups) {
        this.groups = groups;
        if (fragments.get(NavigationMenu.GROUPS.getId()).equals(currentFragment)) {
            ((ListFragment) fragments.get(NavigationMenu.GROUPS.getId())).notifyDataSetChanged();
        }
    }

    public void setTrainings(List<SportEvent> trainings) {
        this.trainings = trainings;
        if (fragments.get(NavigationMenu.TRAINING.getId()).equals(currentFragment)) {
            ((ListFragment) fragments.get(NavigationMenu.TRAINING.getId())).notifyDataSetChanged();
        }
    }

    public void setRaces(List<SportEvent> races) {
        this.races = races;
        if (fragments.get(NavigationMenu.RACES.getId()).equals(currentFragment)) {
            ((ListFragment) fragments.get(NavigationMenu.RACES.getId())).notifyDataSetChanged();
        }
    }

    private void initFragments () {
        fragments = new ArrayList<>();
        for (int i = 0; i < navigationMenu.length; i++) {
            switch (navigationMenu[i]) {
                case USERS:
                    ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
                    queryUsers.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            Toast debug = Toast.makeText(getApplication(), "Users Loaded", Toast.LENGTH_LONG);
                            debug.show();
                            setUsers(objects);
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
                                sportEventType = NavigationMenu.getNavigationIDByString(
                                        sportEvents.get(0).getType(), getApplicationContext());
                                if(sportEventType == NavigationMenu.RACES) {
                                    setRaces(sportEvents);
                                } else if (sportEventType == NavigationMenu.TRAINING) {
                                    setTrainings(sportEvents);
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
        outState.putInt(CURRENT_POSITION_DRAWER, currentPosition);
        super.onSaveInstanceState(outState);
    }

    public ListFragment getCurrentFragment() {
        return currentFragment;
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

    public List<ParseUser> getUsers() {
        return users;
    }

    public List<ParseRole> getGroups() {
        return groups;
    }

    public List<SportEvent> getTrainings() {
        return trainings;
    }

    public List<SportEvent> getRaces() {
        return races;
    }
}
