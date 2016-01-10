package com.chest.blue.filechest;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Elysi on 8/15/2015.
 */
public class DelPubLinksAsyncTask extends AsyncTask<String[],Void,Void> {
    @Override
    protected Void doInBackground(String[]... strings) {

        //TODO
        /*
            Caused by: java.lang.ArrayIndexOutOfBoundsException: length=1; index=1
            at com.chest.blue.filechest.DelPubLinksAsyncTask.doInBackground(DelPubLinksAsyncTask.java:13)
            at com.chest.blue.filechest.DelPubLinksAsyncTask.doInBackground(DelPubLinksAsyncTask.java:8)
         */
        DeletePubLink deletePubLink = new DeletePubLink();
        deletePubLink.DelPubLink(strings[0].toString(),strings[1].toString());

        return null;
    }
}
