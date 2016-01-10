package com.chest.blue.filechest;

/**
 * Created by Blue on 6/6/2015.
 */

// http://www.tutorialspoint.com/android/android_sqlite_database.htm

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
       // Log.v("TAG","Constructor DBHelper" + context.toString());
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS contents");
        //db.execSQL("DROP TABLE IF EXISTS auth");
        db.execSQL(
                "CREATE TABLE contents" +
                        "(primaryid INTEGER PRIMARY KEY, name text, isfolder text, parentfolderid text, folderid text, fileid text, id text, modified text)"
        );
        db.execSQL("CREATE TABLE auth" + "(primaryid TEXT PRIMARY KEY, auth text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contents");
        onCreate(db);
    }


    public void DropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete("contents", null, null);
        //db.delete("auth",null,null);
        db.execSQL("DROP TABLE "/*IF EXISTS*/ +  "contents");
        db.execSQL("DROP TABLE "/*IF EXISTS*/ + "auth");
        context.deleteDatabase(DATABASE_NAME);
        Log.d("DB", "THE DATABASE WAS DELETED");
        db.close();
    }

    public boolean insertContentsFolder(Context context,String name,
                                        String parentfolderid, String folderid,
                                        String isfolder,String id, String modified) {
        Log.v("TAG", "TEST FOLDER");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("TAG", "database can be accessed to write");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name.toString());
        contentValues.put("isfolder", isfolder.toString());
        contentValues.put("parentfolderid", parentfolderid.toString());
        contentValues.put("folderid", folderid.toString());
        contentValues.put("fileid", "false");
        contentValues.put("id", id.toString());
        contentValues.put("modified", modified.toString());
        db.insert("contents", null, contentValues);
        db.close();
        return true;
    }

    public boolean insertContentsFiles(String name, String parentfolderid,
                                       String fileid, String isfolder,
                                       String id, String modified) {
        //Log.v("TAG","TEST FILE");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("TAG", "database can be accessed to write");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name.toString());
        contentValues.put("isfolder", isfolder.toString());
        contentValues.put("parentfolderid", parentfolderid.toString());
        contentValues.put("fileid", fileid.toString());
        contentValues.put("folderid", "false");
        contentValues.put("id",id.toString());
        contentValues.put("modified",modified.toString());
        db.insert("contents", null, contentValues);
        db.close();
        return true;
    }

    public void setAuth(String auth){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("TAG", "the auth is going to be set");
        ContentValues contentValues = new ContentValues();
        contentValues.put("auth", auth.toString());
        db.insert("auth", null, contentValues);
        db.close();
        Log.v("TAG", "The auth was set in DB");
    }

    /*
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM ids WHERE fids=" + id + "", null );
        res.moveToFirst();
        return res;
    }
    */

    public ArrayList<String> getAllFolderIDs() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contents", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        if (array_list.isEmpty()) {
            Log.v("TAG :", "empty list");
        }
        else         Log.v("TAG :", "not empty list");

        db.close();
        return array_list;
    }

    public ArrayList<Map<String,String>> getAllFolderFilesByParentFolderID(String parentfolderid) {
        ArrayList<Map<String,String>> array_list = new ArrayList<Map<String,String>>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contents where parentfolderid='" + parentfolderid + "'", null);
        if(res == null)
        {
            Log.v("TAG","The query is empty");
            return array_list;
        }
        else{
        res.moveToFirst();

        while (res.isAfterLast() == false) {


            Map<String,String> map = new Map<String, String>() {
                @Override
                public void clear() {

                }

                @Override
                public boolean containsKey(Object key) {
                    return false;
                }

                @Override
                public boolean containsValue(Object value) {
                    return false;
                }

                @NonNull
                @Override
                public Set<Entry<String, String>> entrySet() {
                    return null;
                }

                @Override
                public String get(Object key) {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Set<String> keySet() {
                    return null;
                }

                @Override
                public String put(String key, String value) {
                    return null;
                }

                @Override
                public void putAll(Map<? extends String, ? extends String> map) {

                }

                @Override
                public String remove(Object key) {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }

                @NonNull
                @Override
                public Collection<String> values() {
                    return null;
                }
            };
            map.put("name", res.getString(res.getColumnIndex("name")).toString());
            Log.v("TAG", "FOLDER NAME: " + res.getString(res.getColumnIndex("name")).toString());
            map.put("folderid", res.getString(res.getColumnIndex("folderid")).toString());
            Log.v("TAG", "FOLDERID :" + res.getString(res.getColumnIndex("folderid")).toString());
            map.put("parentfolderid", res.getString(res.getColumnIndex("parentfolderid")).toString());
            Log.v("TAG", "PARENTFOLDERID :" + res.getString(res.getColumnIndex("parentfolderid")).toString());
            map.put("isfolder", res.getString(res.getColumnIndex("isfolder")).toString());
            Log.v("TAG", "FOLDER/FILE: " + res.getString(res.getColumnIndex("isfolder")).toString());

            //Log.v("TAG", "FOLDER/FILE: " + res.getString(res.getColumnIndex("isfile")).toString()); // crashes here ????
            //map.put("isfile", res.getString(res.getColumnIndex("isfile")).toString());
            //map.put("id", res.getString(res.getColumnIndex("id")).toString());
            //Log.v("TAG", "FOLDER ID: " + res.getString(res.getColumnIndex("id")).toString());
            array_list.add(map);


            res.moveToNext();
            }
            if (array_list.isEmpty()) {
            Log.v("TAG :", "empty list");
            }
            else         Log.v("TAG :", "not empty list");

            db.close();
            return array_list;
        }
    }

    public ArrayList<Items> getItems(String parentfolderid){

        ArrayList<Items> array_list = new ArrayList<Items>();

        Log.v("TAG","Request items with parentfolderid == " + parentfolderid);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contents where parentfolderid='" + parentfolderid + "'", null);
        if(res == null)
        {
            Log.v("TAG","The query is empty");
            return array_list;
        }
        else
        {
            res.moveToFirst();

            while (res.isAfterLast() == false) {

                Items items = new Items(res.getString(res.getColumnIndex("name")).toString(),
                        res.getString(res.getColumnIndex("folderid")).toString(),
                        res.getString(res.getColumnIndex("fileid")).toString(),
                        res.getString(res.getColumnIndex("parentfolderid")).toString(),
                        res.getString(res.getColumnIndex("modified")).toString());
                array_list.add(items);

                res.moveToNext();
            }
            if (array_list.isEmpty()) {
                Log.v("TAG :", "empty list");
            }
            else {
                Log.v("TAG :", "not empty list");
                Log.v("TAG", "List Size: " + array_list.size());
            }

        }

        db.close();
        return array_list;
    }

    public String getAuth(){
        String auth = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from auth", null);
        if(res == null)
        {
            Log.v("TAG","The query is empty");
            return auth;
        }
        else
        {
            res.moveToFirst();
            //while (!res.isAfterLast()) {
                auth = res.getString(res.getColumnIndex("auth"));
                Log.v("TAG","The auth is: " + auth);
            //}
            db.close();
            return auth;
        }
    }

    public ArrayList<Items> getParentIdFromId(String folderid){

        ArrayList<Items> array_list = new ArrayList<Items>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contents where folderid='" + folderid + "'", null);
        if(res == null)
        {
            Log.v("TAG","The query is empty");
            return array_list;
        }
        else
        {
            res.moveToFirst();

            while (res.isAfterLast() == false) {

                Items items = new Items(res.getString(res.getColumnIndex("name")).toString(),
                        res.getString(res.getColumnIndex("folderid")).toString(),
                        res.getString(res.getColumnIndex("fileid")).toString(),
                        res.getString(res.getColumnIndex("parentfolderid")).toString(),
                        res.getString(res.getColumnIndex("modified")).toString());
                array_list.add(items);

                res.moveToNext();
            }
            if (array_list.isEmpty()) {
                Log.v("TAG :", "empty list");
            }
            else {
                Log.v("TAG :", "not empty list");
                Log.v("TAG", "List Size: " + array_list.size());
            }

        }

        db.close();
        return array_list;
    }

    public void deleteItems(Items items){
        SQLiteDatabase db = this.getWritableDatabase();
        if (items.getId().equals(null))
            db.delete(DATABASE_NAME, "fileid=" + items.getFileId(), null);
        else
            db.delete(DATABASE_NAME, "folderid=" + items.getId(), null);
    }

    public ArrayList<Items> getItemsByName(String name){

        ArrayList<Items> array_list = new ArrayList<Items>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contents where name='" + name + "'", null);
        if(res == null)
        {
            Log.v("TAG","The query is empty");
            return array_list;
        }
        else
        {
            res.moveToFirst();

            while (res.isAfterLast() == false) {

                Items items = new Items(res.getString(res.getColumnIndex("name")).toString(),
                        res.getString(res.getColumnIndex("folderid")).toString(),
                        res.getString(res.getColumnIndex("fileid")).toString(),
                        res.getString(res.getColumnIndex("parentfolderid")).toString(),
                        res.getString(res.getColumnIndex("modified")).toString());
                array_list.add(items);

                res.moveToNext();
            }
            if (array_list.isEmpty()) {
                Log.v("TAG :", "empty list");
            }
            else {
                Log.v("TAG :", "not empty list");
                Log.v("TAG", "List Size: " + array_list.size());
            }

        }

        db.close();
        return array_list;
    }
}