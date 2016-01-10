package com.chest.blue.filechest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Blue on 7/12/2015.
 */
public class ClearCache {

    private static Context context;

    public ClearCache(Context context){
        this.context = context;
    }

    public static void trimCache() {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                Log.v("TAG", dir.toString());
                deleteDir(dir);
            }
            Toast.makeText(context,"The cache was deleted!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context,"Upps. Something went wrong!",Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
