package com.chest.blue.filechest;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Elysi on 8/12/2015.
 */
public class RenameAsyncTask extends AsyncTask<String[], Void, String> {

    private Exception exception = null;
    public String ResultString = null;
    public int response_code;
    public String response_string;

    protected String doInBackground(String[]... arg) {

        if(arg[0].equals("0")){
            RenameFolder renameFolder = new RenameFolder();
            renameFolder.renameFolder(arg[1].toString(),arg[2].toString(),arg[3].toString());
            if(renameFolder.GetError().equals("")){
                return "";
            }else {
                Log.v("TAG","Rename folder error: " + renameFolder.GetError());
                return renameFolder.GetError();
            }
        }
        else {
            RenameFile renameFile = new RenameFile();
            renameFile.renameFile(arg[1].toString(), arg[2].toString(), arg[3].toString());
            if(renameFile.GetError().equals("")){
                return "";
            }else {
                Log.v("TAG","Rename file error:" + renameFile.GetError());
                return renameFile.GetError();
            }
        }
    }

    protected void onPostExecute(String stream_url) {
        // TODO: check this.exception
        // TODO: do something with the feed
        if (this.exception != null)
            this.exception.printStackTrace();

        this.ResultString = stream_url;
    }

}