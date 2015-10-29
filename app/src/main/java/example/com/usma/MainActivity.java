package example.com.usma;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
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

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener{

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    public NavigationMenu[] navigationMenu = {NavigationMenu.RACES, NavigationMenu.TRAINING,
            NavigationMenu.GROUPS, NavigationMenu.LICENCE, NavigationMenu.USERS};
    private List<Fragment> fragments;
    private ListFragment currentFragment;

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
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        name = ParseUser.getCurrentUser().getString(User.FIRSTNAME) + " "
                + ParseUser.getCurrentUser().getString(User.NAME);
        email = ParseUser.getCurrentUser().getEmail();
        buttonNew = (FloatingActionButton) findViewById(R.id.button_new);




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


        initFragments();

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
            final Fragment fragment = fragments.get(position-1);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            buttonNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListFragment)fragment).newItem();
                }
            });
            currentFragment = (ListFragment) fragment;
        }
        // update selected item title, then close the drawer
        //setTitle(((TodoListFragment) fragment).getTodoListRole().getString(Todo.LIST_NAME_KEY));
        //TODO
        getDrawer().closeDrawers();
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    private void initFragments () {
        fragments = new ArrayList<Fragment>();
        for (int i = 0; i < navigationMenu.length; i++) {
            switch (navigationMenu[i]) {
                case USERS:
                    ListFragmentUsers fragment = ListFragmentUsers.newInstance();
                    fragments.add(fragment);
                    ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
                    queryUsers.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> listUsers, ParseException e) {
                            ((ListFragmentUsers)fragments.get(NavigationMenu.USERS.getId())).setUsers(listUsers);
                            Toast debug = Toast.makeText(getApplication(), "Users Loaded", Toast.LENGTH_LONG);
                            debug.show();
                        }
                    });

                    break;
                case GROUPS:
                    ListFragmentGroups fragmentGroups = ListFragmentGroups.newInstance();
                    fragments.add(fragmentGroups);
                    ParseQuery<ParseRole> queryGroups = ParseRole.getQuery();
                    queryGroups.findInBackground(new FindCallback<ParseRole>() {
                        @Override
                        public void done(List<ParseRole> listRole, ParseException e) {
                            ((ListFragmentGroups)fragments.get(NavigationMenu.GROUPS.getId())).setGroups(listRole);
                            Toast debug = Toast.makeText(getApplication(), "Groups Loaded", Toast.LENGTH_LONG);
                            debug.show();
                        }
                    });

                    break;
                case RACES:
                    ListFragmentRace fragmentRaces = ListFragmentRace.newInstance();
                    fragments.add(fragmentRaces);

                    break;
                case TRAINING:
                    ListFragmentTraining fragmentTraining = ListFragmentTraining.newInstance();
                    fragments.add(fragmentTraining);
                    break;

                case LICENCE:
                    ListFragmentLicence fragmentLicence = ListFragmentLicence.newInstance();
                    fragments.add(fragmentLicence);
                    break;

            }
        }
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
}
