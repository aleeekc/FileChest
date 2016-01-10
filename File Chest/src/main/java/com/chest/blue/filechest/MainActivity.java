package com.chest.blue.filechest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.SparseBooleanArray;

import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.SearchView;
import android.widget.Toast;

import android.app.ProgressDialog;

import android.support.v4.app.ActionBarDrawerToggle;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    String inFolder = "0";
    List<Integer> selectedItems;
    List<Items> itemsList = new ArrayList<Items>();
    MyItemAdapter NewnewsEntryAdapter;
    String name;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        inFolder = intent.getStringExtra("inFolder");

        Log.v("TAG", "I am in folder with id: " + inFolder);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        final ListView newsEntryListView = (ListView) findViewById(R.id.list);
        newsEntryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        final MyItemAdapter newsEntryAdapter = new MyItemAdapter(this, R.layout.listview_item, getNewsEntries("0"));
        SetAdapter(newsEntryAdapter);
        newsEntryListView.setAdapter(newsEntryAdapter);

        /*
        // Populate the list, through the adapter
        for (final Items entry : getNewsEntries("0")) {
            newsEntryAdapter.add(entry);
            Log.v("TAG", "Name: " + entry.getName());
        }

*/


        newsEntryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                //final String item = (String) parent.getItemAtPosition(position);

                //final MyItemAdapter newAdapter = new MyItemAdapter(getApplicationContext(), R.layout.listview_item,getNewsEntries(inFolder));


                List<Items> list = getNewsEntries(inFolder);

                if (list.get(position).getId() != null) {
                    inFolder = list.get(position).getId();
                }

                /*
                // Remove all old items
                for(int i=0;i<list.size();i++) {
                    Log.v("TAG","Removing item: " + list.get(i).getName());
                    newsEntryAdapter.remove(list.get(i));
                }
                */
                NewnewsEntryAdapter.removeAll(list);

                for (final Items entry : getNewsEntries(inFolder)/*list.get(position).getId())*/) {
                    NewnewsEntryAdapter.add(entry);
                    Log.v("TAG", "Name: " + entry.getName());
                }

                //newsEntryListView.setAdapter(newsEntryAdapter);
                //newsEntryAdapter.notifyDataSetChanged();
                view.setAlpha(1);


            }
        });
/*
        newsEntryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                //parent.setItemChecked(position, true);

                view.setBackgroundColor(Color.rgb(200, 200, 200));
                view.findViewById(R.id.checkicon).setVisibility(View.VISIBLE);
                //view.setSelected(true);

                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);

                //selectedItems.add(pos);
                Log.v("TAG", "Item selected at position: " + pos);
                newsEntryAdapter.notifyDataSetChanged();

                return true;
            }
        });
*/
        //-----------------------------------------------------------
        newsEntryListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

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

                Boolean Flag_User_Choice;

                // Calls getSelectedIds method from ListViewAdapter Class
                final SparseBooleanArray selected = newsEntryAdapter
                        .getSelectedIds();

                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        //SparseBooleanArray selected = newsEntryAdapter
                        //      .getSelectedIds();

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        // Captures all selected ids with a loop
                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                Items selecteditem = newsEntryAdapter
                                                        .getItem(selected.keyAt(i));
                                                // Remove selected items following the ids
                                                newsEntryAdapter.remove(selecteditem);

                                                DBHelper dbHelper = new DBHelper(MainActivity.this);

                                                // TODO MAKE SOMEKIND OF PROGRESS SO THAT THE ASYNC TASK CAN FINISH THEIR WORK AND THEN REFRESH/RESYNC THE ACTIVITY

                                                if(selecteditem.getFileId().equals("")) {
                                                    String[] arg = {"1",selecteditem.getId(),dbHelper.getAuth()};
                                                    new DelAsyncTask().execute(arg);
                                                }else {
                                                    String[] arg = {"0",selecteditem.getFileId(),dbHelper.getAuth()};
                                                    new DelAsyncTask().execute(arg);
                                                }
                                            }
                                        }

                                        dialog.dismiss();

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        dialog.dismiss();

                                        break;
                                }
                            }
                        };

                        Items newselecteditem = newsEntryAdapter.getItem(selected.keyAt(0));

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to delete " + newselecteditem.getName() +  " ?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                        // Close CAB
                        mode.finish();
                        return true;

                    case R.id.Rename:
                        if (selected.size() > 1)
                            Toast.makeText(getApplicationContext(), "Only one item at a time can be renamed!", Toast.LENGTH_SHORT).show();
                        else {
                            // TODO RETEST

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(MainActivity.this);
                            View promptsView = li.inflate(R.layout.dialog, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    MainActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

                                                    if(userInput.getText().equals(null) || userInput.getText().equals("")){
                                                        Toast.makeText(MainActivity.this,"Please input the new name!",Toast.LENGTH_SHORT).show();
                                                    }else {

                                                        //TODO RETEST

                                                        Items selectedItem = newsEntryAdapter
                                                                .getItem(selected.keyAt(0));

                                                        DBHelper DB = new DBHelper(getApplicationContext());

                                                        if (selectedItem.getFileId() == "" || selectedItem.getFileId() == null) {
                                                            // ITS A FOLDER
                                                            String[] arr = {"0", selectedItem.getId(), userInput.getText().toString(), DB.getAuth()};
                                                            new RenameAsyncTask().execute(arr);
                                                        } else {
                                                            // ITS A FILE
                                                            String[] arr = {"1", selectedItem.getFileId(), userInput.getText().toString(), DB.getAuth()};
                                                            new RenameAsyncTask().execute(arr);
                                                        }

                                                        // PROGRESS DIALOG
                                                        ProgressDialog pdialog = new ProgressDialog(MainActivity.this);
                                                        pdialog.setCancelable(true);
                                                        pdialog.setMessage("Loading ....");
                                                        pdialog.show();

                                                        Resync resync = new Resync(MainActivity.this);

                                                        pdialog.dismiss();

                                                        Log.v("TAG", "The resync was done!");

                                                        Intent inten = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(inten);
                                                    }

                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();

                        }
                    case R.id.CopyLink:
                        if (selected.size() > 1)
                            Toast.makeText(getApplicationContext(), "Please select one item to copy a link", Toast.LENGTH_SHORT);
                        else {
                            DBHelper db = new DBHelper(getApplicationContext());
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    Items selecteditem = newsEntryAdapter.getItem(selected.keyAt(i));
                                    String text;

//-------------------------------- ////// LATER MAKE THIS WORK WITH THE ASYNCTASK !!!!!!!!!!!!!!!!

                                    if (selecteditem.getFileId().equals("false")) {
                                        Log.v("TAG", "fileid = " + selecteditem.getFileId());
                                        text = "folderid=" + selecteditem.getId();

                                        GetDownloadlinkFolder downloadLink = new GetDownloadlinkFolder(text, db.getAuth());
                                        downloadLink.execute();

                                        if (!downloadLink.Error().equals("")) {
                                            Toast.makeText(getApplicationContext(), downloadLink.Error(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText(selecteditem.getName(), downloadLink.link());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getApplicationContext(), "Download Link copied!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (selecteditem.getId().equals("false")) {

                                        text = "fileid=" + selecteditem.getFileId();

                                        GetDownloadLink downloadLink = new GetDownloadLink(text, db.getAuth());
                                        downloadLink.execute();

                                        if (!downloadLink.Error().equals("")) {
                                            Toast.makeText(getApplicationContext(), downloadLink.Error(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText(selecteditem.getName(), downloadLink.link());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getApplicationContext(), "Download Link copied!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }

                    case R.id.GetDownloadLink:
                        if (selected.size() > 1)
                            Toast.makeText(getApplicationContext(), "Please select one item to copy a link", Toast.LENGTH_SHORT);
                        else {
                            DBHelper db = new DBHelper(getApplicationContext());
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    Items selecteditem = newsEntryAdapter.getItem(selected.keyAt(i));
                                    String text;

//-------------------------------- ////// LATER MAKE THIS WORK WITH THE ASYNCTASK !!!!!!!!!!!!!!!!

                                    if (selecteditem.getFileId().equals("false")) {
                                        Log.v("TAG", "fileid = " + selecteditem.getFileId());
                                        text = "folderid=" + selecteditem.getId();

                                        GetDownloadlinkFolder downloadLink = new GetDownloadlinkFolder(text, db.getAuth());
                                        downloadLink.execute();

                                        Log.v("TAG", "error = " + downloadLink.Error());
                                        if (!downloadLink.Error().equals("")) {
                                            Toast.makeText(getApplicationContext(), downloadLink.Error(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT, downloadLink.link());
                                            sendIntent.setType("text/plain");
                                            startActivity(Intent.createChooser(sendIntent, "Share " + selecteditem.getName() + " using:"));
                                        }

                                    } else if (selecteditem.getId().equals("false")) {

                                        text = "fileid=" + selecteditem.getFileId();

                                        GetDownloadLink downloadLink = new GetDownloadLink(text, db.getAuth());
                                        downloadLink.execute();

                                        if (!downloadLink.Error().equals("")) {
                                            Toast.makeText(getApplicationContext(), downloadLink.Error(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT, downloadLink.link());
                                            sendIntent.setType("text/plain");
                                            startActivity(sendIntent);
                                            Toast.makeText(getApplicationContext(), "Download Link copied in clipboard!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }


                    case R.id.DownloadFile:
                        ///////////////////////////////// DOWNLOAD A FILE
                        if (selected.size() > 1)
                            Toast.makeText(getApplicationContext(), "Please select one item to copy a link", Toast.LENGTH_SHORT).show();
                        else {
                            DBHelper db = new DBHelper(getApplicationContext());
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    Items selecteditem = newsEntryAdapter.getItem(selected.keyAt(i));
                                    if (!selecteditem.getFileId().equals("false")) {
                                        Download download = new Download();
                                        DBHelper dbHelper = new DBHelper(getApplicationContext());

                                        /*
                                        download.GetGownLink(getApplicationContext(), dbHelper.getAuth(), selecteditem.getFileId());
                                        String file_url = download.GetURL();
                                        */

                                        String[] arr = {selecteditem.getFileId().toString(), dbHelper.getAuth()};

                                        Don don = new Don(arr, getApplicationContext());
                                        String file_url = don.GetUrl();

                                        if (file_url.equals("")) {
                                            Log.v("TAG", "Something went wrong! The download url is: " + file_url);
                                        } else {
                                            Log.v("TAG", "The download url is: " + file_url);
                                        }

                                        //new DownloadAsyncTask().execute(arr);

                                        Log.v("TAG", "URL to file: " + file_url);

                                        new DownloadFileFromURL().execute(file_url);
                                    }
                                }
                            }
                        }
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                SparseBooleanArray selected = newsEntryAdapter
                        .getSelectedIds();
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

        //-----------------------------------------------------------
/*
        newsEntryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                //final String item = (String) parent.getItemAtPosition(position);

                newsEntryAdapter.clear();

                List<Items> list = getNewsEntries(inFolder);
                inFolder = list.get(position).getId();

                for (final Items entry : getNewsEntries(list.get(position).getId())) {
                    newsEntryAdapter.add(entry);
                    Log.v("TAG", "Name: " + entry.getName());
                }

                newsEntryAdapter.notifyDataSetChanged();
                view.setAlpha(1);


            }
        });
        ;

        newsEntryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                //parent.setItemChecked(position, true);

                if (view.isSelected()) {
                    view.setBackgroundColor(Color.WHITE);
                    view.findViewById(R.id.checkicon).setVisibility(View.INVISIBLE);
                    view.setSelected(false);

                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(100);

                    Log.v("TAG", "Item unselected at position: " + pos);
                    newsEntryAdapter.notifyDataSetChanged();

                }else {
                    view.setBackgroundColor(Color.rgb(200, 200, 200));
                    view.findViewById(R.id.checkicon).setVisibility(View.VISIBLE);
                    view.setSelected(true);

                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(100);

                    //selectedItems.add(pos);
                    Log.v("TAG", "Item selected at position: " + pos);
                    newsEntryAdapter.notifyDataSetChanged();
                }

                return true;
            }
        });

        */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (inFolder.equals("0")) {
                    // Set Activity in Background
                    moveTaskToBack(true);
                } else {
                    // DON"T KNOW IF IT WORKS
                    ListView newsEntryListView = (ListView) findViewById(R.id.list);
                    DBHelper db = new DBHelper(getApplicationContext());
                    List<Items> items = db.getParentIdFromId(inFolder);

                    /*
                    final MyItemAdapter newsEntryAdapter = new MyItemAdapter(this, R.layout.listview_item,getNewsEntries(items.get(0).getParentId()));
                    newsEntryListView.setAdapter(newsEntryAdapter);
                    */

                    String parentfolderid = items.get(0).getParentId();

                    // Remove all old items
                    for (int i = 0; i < items.size(); i++) {
                        Log.v("TAG", "Removing item: " + items.get(i).getName());
                        NewnewsEntryAdapter.remove(items.get(i));
                    }

                    NewnewsEntryAdapter.addAll(getNewsEntries(parentfolderid));

                    inFolder = items.get(0).getParentId();
                    Log.v("TAG", "Returning to folderid: " + inFolder);

                    //newsEntryAdapter.notifyDataSetChanged();

                    //Intent intent = new Intent(this, MainActivity.class);
                    //intent.putExtra("inFolder", inFolder);
                    //startActivity(intent);

                }
                return true;
        }
        return false;
    }

    private List<Items> getNewsEntries(String folderid) {

        DBHelper db = new DBHelper(getApplicationContext());
        return db.getItems(folderid);
    }

    public void SetAdapter(MyItemAdapter newsEntryAdapter) {
        this.NewnewsEntryAdapter = newsEntryAdapter;
    }

    public MyItemAdapter ReturnAdapter(MyItemAdapter newsEntryAdapter) {
        return NewnewsEntryAdapter;
    }

    /**
     * Showing Dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
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
                Log.v("TAG","In menu: " + mTitle);
                break;

            case 2: // CLEAR CACHE
                //mTitle = getString(R.string.title_section2);
                ClearCache cache = new ClearCache(getApplicationContext());
                cache.trimCache();
                Log.v("TAG", "The cache was cleared");
                break;

            case 3: // LINKS
                mTitle = getString(R.string.title_section3);
                Log.v("TAG","In menu: " + mTitle);

                DBHelper db = new DBHelper(getApplicationContext());
                String[] ar = {db.getAuth()};
                Log.v("TAG", "The auth from the db is: " + ar);

                DonLinks obj = new DonLinks(ar,MainActivity.this);

                break;

            case 4: //LOGOUT

                // WORKS !!!
                mTitle = getString(R.string.title_section4);
                Log.v("TAG","In menu: " + mTitle);
                //Logout logout = new Logout();
                DBHelper dbH = new DBHelper(getApplicationContext());
                //logout.logOut(db.getAuth(), getApplicationContext());
                String arg = dbH.getAuth();
                Log.v("TAG","The auth from the db is: " + arg);
                new Logout().execute(arg);
                dbH.DropTables();
                //Log.v("TAG", "LOGOUT RESPONSE" + logout.logout_response());
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case 5: // GALLERY
                mTitle = getString(R.string.title_section5);
                Log.v("TAG","In menu: " + mTitle);
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
                ProgressDialog pdialog = new ProgressDialog(MainActivity.this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Loading ....");
                pdialog.show();

                Resync resync = new Resync(getApplicationContext());

                // CLOSE PROGRESS
                pdialog.dismiss();

                Log.v("TAG","The resync is done! It was clicked from the navigation drawer!");

                Intent inten = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inten);

                Log.v("TAG","THIS MESSAGE SHOULD NEVER BE SHOWN!!! ");

                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setIcon(R.drawable.treasure_chest);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                    .getActionView();

//            searchView.setSearchableInfo(searchManager
//                    .getSearchableInfo(getComponentName()));

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent AboutIntent = new Intent(this,About.class);
            startActivity(AboutIntent);
            return true;
        }
        if (id == R.id.action_search) {
            // search action
            return true;
        }

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }
    //--------------------------------LIFECYCLE -------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        //notify("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //notify("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //notify("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //notify("onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //notify("onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //notify("onSaveInstanceState");
    }

    private void notify(String methodName) {
        String name = this.getClass().getName();
        String[] strings = name.split("\\.");
        Notification noti = new Notification.Builder(this)
                .setContentTitle(methodName + " " + strings[strings.length - 1]).setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(name).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), noti);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//http://www.androidhive.info/2012/04/android-downloading-file-by-showing-progress-bar/

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

//!!!!!!!!!!!!!!!!!!!//////////// NEED TO BE ADDED FORMAT OF THE FILE E.G .JPEG AFTER FILENAME
                // Output stream
                OutputStream output = new FileOutputStream("/sdcard/.FileChest/" /*+ getName()*/);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            // String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }
}
