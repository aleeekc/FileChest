package com.chest.blue.filechest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Blue on 6/13/2015.
 */
public class ListFolderParser {

    protected String JSON;
    ArrayList list = new ArrayList();
    Context context;

    public ListFolderParser(String json, Context context){
        this.JSON = json;
        this.context = context;
    }
/*
    public boolean isQuerySuccesfull() {
        boolean success = false;
        try {
            if (this.JSON == null) {
                return false;
            }
            JSONObject json = new JSONObject(this.JSON);
            if (json.has("folderid")) {
                success = true;
            }

            return success;
        } catch (JSONException e) {
            e.printStackTrace();
            return success;
        }

    }

    public String getJSONError() {
        if (isQuerySuccesfull()) {
            return null;
        }
        try {
            JSONObject error = (new JSONObject(this.JSON)).getJSONObject("error");
            if (!error.has("error")) {
                throw new JSONException("no error message");
            }
            return error.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
*/
    public void ParseToDB(){

       // if (isQuerySuccesfull()) {
         //   Log.v("TAG","query was succsessful");
        try {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(JSON);

                JSONObject jsonObjectMeta = (JSONObject) jsonObject.get("metadata");
                JSONArray contents = (JSONArray) jsonObjectMeta.get("contents");

                ParseContexts(contents);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.v("TAG", "Couldn't parse the hash of a file");
            }
        }catch (Exception e) {
                e.printStackTrace();

        }
        //    Log.v("TAG","query NOT was succsessful");
      //  }
    }

    public void ParseContexts(JSONArray contents){

        Log.v("TAG","The contents of a response was send to the parser");
        Iterator i = contents.iterator();
        if (!list.isEmpty()) {
            list.clear();
        }

        DBHelper dbHelper = new DBHelper(context);

        //try {
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();

                if (innerObj.containsKey("folderid")) {
                    //FOLDER NAME
                    Log.v("TAG: ", innerObj.get("name").toString());
                    list.add(innerObj.get("name").toString());

                    //PARENT_FOLDER_ID
                    Log.v("TAG: ", innerObj.get("parentfolderid").toString());
                    list.add(innerObj.get("parentfolderid").toString());

                    //FOLDER_ID
                    Log.v("TAG: ", innerObj.get("folderid").toString());
                    list.add(innerObj.get("folderid").toString());

                    //IS_FOLDER
                    Log.v("TAG: ", innerObj.get("isfolder").toString());
                    list.add(innerObj.get("isfolder").toString());

                    Log.v("TAG: ", innerObj.get("id").toString());
                    list.add(innerObj.get("id").toString());

                    Log.v("TAG: ", innerObj.get("modified").toString());
                    list.add(innerObj.get("modified").toString());

                    //SENT_LIST_TO_DB
                    dbHelper.insertContentsFolder(context, list.get(0).toString(),
                                                    list.get(1).toString(), list.get(2).toString(),
                                                    list.get(3).toString(),list.get(4).toString(),
                                                    list.get(5).toString()); //{
                    //ListFoldersToDB(list);

                    //FOLDER_CONTENTS
                    Log.v("TAG: ", innerObj.get("contents").toString());
                    ParseContexts((JSONArray)innerObj.get("contents"));


                } else if(innerObj.containsKey("fileid")){
                    //FILE_NAME
                    Log.v("TAG: ", innerObj.get("name").toString());
                    list.add(innerObj.get("name").toString());

                    //PARENT_FOLDER_ID
                    Log.v("TAG: ", innerObj.get("parentfolderid").toString());
                    list.add(innerObj.get("parentfolderid").toString());

                    //FILE_ID
                    Log.v("TAG: ", innerObj.get("fileid").toString());
                    list.add(innerObj.get("fileid"));//.toString());

                    //IS_FOLDER
                    Log.v("TAG: ", innerObj.get("isfolder").toString());
                    list.add(innerObj.get("isfolder").toString());

                    Log.v("TAG: ", innerObj.get("id").toString());
                    list.add(innerObj.get("id").toString());

                    Log.v("TAG: ", innerObj.get("modified").toString());
                    list.add(innerObj.get("modified").toString());

                    //SENT_LIST_TO_DB
                    //ListFilesToDB(list);
                    dbHelper.insertContentsFiles(list.get(0).toString(),
                            list.get(1).toString(),
                            list.get(2).toString(),
                            list.get(3).toString(),
                            list.get(4).toString(),
                            list.get(5).toString());

                }else {
                    Log.v("TAG","Empty folder");
                }
            }

        //} catch (Exception e) {
          //  e.printStackTrace();
        //}
    }

}
