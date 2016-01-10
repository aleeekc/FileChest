package com.chest.blue.filechest;

/**
 * Created by Elysi on 8/9/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsActivity extends Activity {

    private TextView txtQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // get the action bar
        ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v("TAG","The search was for: " + query);

            DBHelper db = new DBHelper(getApplicationContext());
            ArrayList<Items> items =  db.getItemsByName(query);

            final ListView newsEntryListView = (ListView) findViewById(R.id.list);
            newsEntryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            final MyItemAdapter newsEntryAdapter = new MyItemAdapter(this, R.layout.listview_item, items);
            newsEntryListView.setAdapter(newsEntryAdapter);

            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
            //txtQuery.setText("Search Query: " + query);

        }

    }
}
