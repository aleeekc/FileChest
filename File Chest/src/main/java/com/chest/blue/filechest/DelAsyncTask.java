package com.chest.blue.filechest;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Elysi on 8/14/2015.
 */
public class DelAsyncTask extends AsyncTask<String[],Void,Void> {

    @Override
    protected Void doInBackground(String[]... arg) {

        if(arg[0].equals("1")){
            DeleteFile deleteFile = new DeleteFile();
            deleteFile.DelFile(arg[1].toString(),arg[2].toString());

            if(deleteFile.GetError()!=""){
                // TODO SHOW ERROR MESSAGE
            }

        }
        else {
            DeleteFolder deleteFolder = new DeleteFolder();
            deleteFolder.DelFolder(arg[1].toString(),arg[2].toString());

            if(deleteFolder.GetError()!=""){
                // TODO SHOW ERROR MESSAGE
            }
        }
        return null;
    }
}
