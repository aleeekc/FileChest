package com.chest.blue.filechest;

import android.content.Context;

/**
 * Created by Elysi on 8/9/2015.
 */
public class Don  implements DownloadAsyncTask.TheInterface{

    String Url = "";

    public Don(String[] arr,Context context) {
        new DownloadAsyncTask(context).execute(arr);
    }

    @Override
    public void theMethod(String result) {
        Url = result;
    }

    public String GetUrl(){
        return Url;
    }

}
