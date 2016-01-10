package com.chest.blue.filechest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class LinksActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    MyLinksAdapter NewnewsEntryAdapter;
    List<Links> list = new ArrayList<Links>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "Links";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        final ListView newsEntryListView = (ListView) findViewById(R.id.list);
        newsEntryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        final MyLinksAdapter newsEntryAdapter = new MyLinksAdapter(this, R.layout.listview_item, getNewsEntries());
        newsEntryListView.setAdapter(newsEntryAdapter);


        //------------------------------------------------------------------

        newsEntryListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = newsEntryListView.getCheckedItemCount();
                // Set the CAB title according to total checked items

                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                newsEntryAdapter.toggleSelection(position);
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Calls getSelectedIds method from ListViewAdapter Class
                SparseBooleanArray selected = newsEntryAdapter
                        .getSelectedIds();

                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        //SparseBooleanArray selected = newsEntryAdapter
                        //      .getSelectedIds();


                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Links selecteditem = newsEntryAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                newsEntryAdapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                SparseBooleanArray selected = newsEntryAdapter
                        .getSelectedIds();
                //TODO MAKE THE ACTIONS FOR THE LINKS
                if (selected.size() > 1)
                    mode.getMenuInflater().inflate(R.menu.activity_main_menu_multiple, menu);
                else
                    mode.getMenuInflater().inflate(R.menu.activity_main_menu_single, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                newsEntryAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });


    }

    private List<Links> getNewsEntries() {

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");

        PubLinksParser parser = new PubLinksParser(response);

        List<String> links = parser.GetPubLink();
        List<String> linkids = parser.GetPubLinkId();
        List<String> linknames = parser.GetPubLinkName();

        if (!links.equals(null) || !links.equals("")) {

            // TODO NULLPOINTER EXCEPTION HERE !!!!!!!!!!!!!!
            Iterator i = parser.GetPubLink().iterator();

            for (int j = 0; j < links.size(); j++) {
                Log.v("TAG", linknames.get(j).toString());
                Log.v("TAG", linkids.get(j));
                Log.v("TAG", links.get(j));
                Links linkslist = new Links(linknames.get(j), linkids.get(j), links.get(j));
                list.add(linkslist);
            }
        } else {
            Toast.makeText(LinksActivity.this, parser.GetError(), Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                moveTaskToBack(true);

                return true;
        }
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1: // HOME ACTIVITY
                mTitle = getString(R.string.title_section1);
                Log.v("TAG", "In menu: " + mTitle);
                Intent intentMain = new Intent(this,MainActivity.class);
                intentMain.putExtra("inFolder","0");
                startActivity(intentMain);

                break;

            case 2: // CLEAR CACHE
                //mTitle = getString(R.string.title_section2);
                ClearCache cache = new ClearCache(getApplicationContext());
                cache.trimCache();
                Log.v("TAG", "The cache was cleared");
                break;

            case 3: // LINKS

                mTitle = getString(R.string.title_section3);
                Log.v("TAG", "In menu: " + mTitle);

                DBHelper db = new DBHelper(LinksActivity.this);
                String[] ar = {db.getAuth()};
                Log.v("TAG", "The auth from the db is: " + ar);

                DonLinks obj = new DonLinks(ar,LinksActivity.this);

                Intent linkIntent = new Intent(this, LinksActivity.class);
                linkIntent.putExtra("response", obj.GetResponse());
                startActivity(linkIntent);

                break;

            case 4: //LOGOUT

                // WORKS !!!
                mTitle = getString(R.string.title_section4);
                Log.v("TAG", "In menu: " + mTitle);
                //Logout logout = new Logout();
                DBHelper dbH = new DBHelper(getApplicationContext());
                //logout.logOut(db.getAuth(), getApplicationContext());
                String arg = dbH.getAuth();
                Log.v("TAG", "The auth from the db is: " + arg);
                new Logout().execute(arg);
                dbH.DropTables();
                //Log.v("TAG", "LOGOUT RESPONSE" + logout.logout_response());
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case 5: // GALLERY
                mTitle = getString(R.string.title_section5);
                Log.v("TAG", "In menu: " + mTitle);
                //GET IMAGE IDs
                ListPhotoes list = new ListPhotoes();
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                list.listPhoto(getApplicationContext(), dbHelper.getAuth());

                //SET IMAGE IDs
                ArrayList<String> fileids = list.GetPhotoIds();
                Iterator it = fileids.iterator();

                ArrayList<String> fileURLs = null;

                //GET DOWNLOAD URLs WITH FILE IDs
                while (it.hasNext()) {
                    String[] arr = {it.next().toString(), dbHelper.getAuth()};
                    Don don = new Don(arr, getApplicationContext());
                    String file_url = don.GetUrl();
                    fileURLs.add(file_url);
                }

                //TODO - SEND THE IMAGE URLS TO THE GALLERY

                break;

            case 6: //RESYNC

                // IDK IF IT WORKS
                mTitle = getString(R.string.title_section6);
                Log.v("TAG", "In menu: " + mTitle);

                // PROGRESS DIALOG
                ProgressDialog pdialog = new ProgressDialog(LinksActivity.this);
                pdialog.setCancelable(true);
                pdialog.setMessage("Loading ....");
                pdialog.show();

                Resync resync = new Resync(getApplicationContext());

                // CLOSE PROGRESS
                pdialog.dismiss();

                Intent inten = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inten);

                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.links, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent AboutIntent = new Intent(this,About.class);
            startActivity(AboutIntent);
            return true;
        }

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_links, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((LinksActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
