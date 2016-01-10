package com.chest.blue.filechest;

import android.content.Context;

/**
 * Created by Elysi on 8/10/2015.
 */
public class DonLinks implements Listpublinks.TheInterface{

    String Response = "";

    public DonLinks(String[] arr,Context context) {
        new Listpublinks(context).execute(arr);
    }

   // @Override
   // public void theMethod(String result) {
   //     Response = result;
   // }

    public String GetResponse(){
        return Response;
    }

}
