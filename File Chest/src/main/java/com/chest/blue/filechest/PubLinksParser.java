package com.chest.blue.filechest;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Elysi on 8/10/2015.
 */
public class PubLinksParser {

    public ArrayList<String> linklist = new ArrayList<String>();
    public ArrayList<String> linkid = new ArrayList<String>();
    public ArrayList<String> linkname = new ArrayList<String>();
    String error;

    public PubLinksParser(String response_string){
        JSONParser parser = new JSONParser();

        try {

            response_string = response_string.substring(4,response_string.length());

            JSONObject jsonObject = (JSONObject) parser.parse(response_string);



            //TODO PARSING PROBLEM HERE
            /*

08-14 09:06:12.150    1648-1648/com.chest.blue.filechest W/System.err﹕ java.lang.NullPointerException
08-14 09:06:12.160    1648-1648/com.chest.blue.filechest W/System.err﹕ at com.chest.blue.filechest.PubLinksParser.<init>(PubLinksParser.java:145)
08-14 09:06:12.160    1648-1648/com.chest.blue.filechest W/System.err﹕ at com.chest.blue.filechest.LinksActivity.getNewsEntries(LinksActivity.java:144)
08-14 09:06:12.160    1648-1648/com.chest.blue.filechest W/System.err﹕ at com.chest.blue.filechest.LinksActivity.onCreate(LinksActivity.java:59)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.Activity.performCreate(Activity.java:5231)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1087)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2148)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2233)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.ActivityThread.access$800(ActivityThread.java:135)
08-14 09:06:12.170    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1196)
08-14 09:06:12.190    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.os.Handler.dispatchMessage(Handler.java:102)
08-14 09:06:12.190    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.os.Looper.loop(Looper.java:136)
08-14 09:06:12.190    1648-1648/com.chest.blue.filechest W/System.err﹕ at android.app.ActivityThread.main(ActivityThread.java:5001)
08-14 09:06:12.200    1648-1648/com.chest.blue.filechest W/System.err﹕ at java.lang.reflect.Method.invokeNative(Native Method)
08-14 09:06:12.210    1648-1648/com.chest.blue.filechest W/System.err﹕ at java.lang.reflect.Method.invoke(Method.java:515)
08-14 09:06:12.210    1648-1648/com.chest.blue.filechest W/System.err﹕ at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:785)
08-14 09:06:12.210    1648-1648/com.chest.blue.filechest W/System.err﹕ at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:601)
                 */

            /*
            {
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "result": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "publinks": [
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ {
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "code": "kZvCX0ZJ6j0a4Jr3cXt8TCkdyK5eSAFv89k",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "link": "https:\/\/my.pcloud.com\/publink\/show?code=kZvCX0ZJ6j0a4Jr3cXt8TCkdyK5eSAFv89k",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Thu, 13 Aug 2015 10:17:22 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "linkid": 1148442,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "traffic": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Thu, 13 Aug 2015 10:17:22 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "downloads": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "metadata": {
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "name": "My Videos",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "ismine": true,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "thumb": false,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "comments": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "id": "d36560491",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "isshared": false,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "icon": 20,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "isfolder": true,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "parentfolderid": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "folderid": 36560491
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ }
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ },
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ {
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "code": "kZTEV0Zx46pondAOqYhSYr5Q4EU0jIg9T9y",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "link": "https:\/\/my.pcloud.com\/publink\/show?code=kZTEV0Zx46pondAOqYhSYr5Q4EU0jIg9T9y",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Fri, 14 Aug 2015 08:21:54 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "linkid": 1152691,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "traffic": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Fri, 14 Aug 2015 08:21:54 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "downloads": 0,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "metadata": {
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "name": "My Music",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "ismine": true,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "thumb": false,
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.130  20016-20057/com.chest.blue.filechest V/TAG﹕ "comments": 0,
08-14 08:19:52.280  20016-20057/com.chest.blue.filechest V/TAG﹕ "id": "d36560489",
08-14 08:19:52.280  20016-20057/com.chest.blue.filechest V/TAG﹕ "isshared": false,
08-14 08:19:52.280  20016-20057/com.chest.blue.filechest V/TAG﹕ "icon": 20,
08-14 08:19:52.280  20016-20057/com.chest.blue.filechest V/TAG﹕ "isfolder": true,
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "parentfolderid": 0,
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "folderid": 36560489
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ }
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ },
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ {
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "code": "kZgEV0ZY7M5oMSg5TB9Dfou6nsOnkY7wVGk",
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "link": "https:\/\/my.pcloud.com\/publink\/show?code=kZgEV0ZY7M5oMSg5TB9Dfou6nsOnkY7wVGk",
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Fri, 14 Aug 2015 08:21:58 +0000",
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "linkid": 1152692,
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "traffic": 0,
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Fri, 14 Aug 2015 08:21:58 +0000",
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "downloads": 0,
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "metadata": {
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "name": "My Music",
08-14 08:19:52.290  20016-20057/com.chest.blue.filechest V/TAG﹕ "created": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "ismine": true,
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "thumb": false,
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "modified": "Thu, 13 Aug 2015 10:11:41 +0000",
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "comments": 0,
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "id": "d36560489",
08-14 08:19:52.320  20016-20057/com.chest.blue.filechest V/TAG﹕ "isshared": false,
08-14 08:19:52.330  20016-20057/com.chest.blue.filechest V/TAG﹕ "icon": 20,
08-14 08:19:52.330  20016-20057/com.chest.blue.filechest V/TAG﹕ "isfolder": true,
08-14 08:19:52.340  20016-20057/com.chest.blue.filechest V/TAG﹕ "parentfolderid": 0,
08-14 08:19:52.340  20016-20057/com.chest.blue.filechest V/TAG﹕ "folderid": 36560489
08-14 08:19:52.340  20016-20057/com.chest.blue.filechest V/TAG﹕ }
08-14 08:19:52.340  20016-20057/com.chest.blue.filechest V/TAG﹕ }
08-14 08:19:52.350  20016-20057/com.chest.blue.filechest V/TAG﹕ ]
08-14 08:19:52.350  20016-20057/com.chest.blue.filechest V/TAG﹕ }
             */

            Log.v("TAG","Response from piblinks request: " + response_string);

            JSONArray publinks = (JSONArray) jsonObject.get("publinks");

            //Iterator i = publinks.iterator();

            Log.v("TAG",publinks.toJSONString());

            for(int i=0;i<publinks.size();i++)
            {
            //while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) publinks.get(i); //i.next();

                Log.v("TAG",innerObj.toJSONString());

                //LINK
                Log.v("TAG: ", innerObj.get("link").toString());
                linklist.add(innerObj.get("link").toString());

                // LINK ID
                Log.v("TAG: ", innerObj.get("linkid").toString());
                linkid.add(innerObj.get("linkid").toString());

                JSONObject jsonObjectMeta = (JSONObject) innerObj.get("metadata");

                // FOLDER NAME
                Log.v("TAG: ", jsonObjectMeta.get("name").toString());
                linkname.add(jsonObjectMeta.get("name").toString());

            }
            //Log.v("TAG","The auth is -> " + auth);

        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(response_string);
                error = (String) jsonObject.get("error");

                Log.v("TAG","Error: " + error);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public ArrayList<String> GetPubLink(){
        return linklist;
    }

    public ArrayList<String> GetPubLinkName(){
        return linkname;
    }

    public ArrayList<String> GetPubLinkId(){
        return linkid;
    }

    public String GetError(){
        return error;
    }
}
