package com.chest.blue.filechest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Elysi on 8/12/2015.
 */
public class Resync {

    public Resync(Context context) {
        new ResyncAsyncTask().execute(context);
    }
}

class ResyncAsyncTask extends AsyncTask<Context, Void, String> {

    protected String doInBackground(Context... context) {

        DBHelper DBhelp = new DBHelper(context[0]);
        String auth = DBhelp.getAuth();
        DBhelp.DropTables();

        ListFolders listFoldObj = new ListFolders();
        listFoldObj.getFolder(auth);
        Log.v("TAG", "The folders were listed");

        ListFolderParser listFolderParser = new ListFolderParser(listFoldObj.foldersList(), context[0]);
        listFolderParser.ParseToDB();

        DBhelp.setAuth(auth);

        return "";

    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed

    }
}
