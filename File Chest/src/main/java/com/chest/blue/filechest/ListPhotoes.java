package com.chest.blue.filechest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Elysi on 8/9/2015.
 */
public class ListPhotoes {
    String response_string = "";
    int response_code;
    ArrayList<String> fileidlist  = null;

    public void listPhoto(Context context, String auth) {

        HttpClient client = new DefaultHttpClient();
        try {
            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());
            //https://api.pcloud.com/search?query=&offset=0&limit=600&iconformat=id&category=1&auth=RfI1HXZkpokZccNLnW7gd2yTi5u2BHJoNQS2PVby
            HttpGet GET = new HttpGet("https://api.pcloud.com/search?query=&offset=0&limit=600&iconformat=id&category=1&auth=" + auth);


            Log.v("TAG", "Check if it works !!!");
            HttpResponse response = client.execute(GET);
            Log.v("TAG",response.toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            response_code = response.getStatusLine().getStatusCode();

            String line = "";
            while ((line = rd.readLine()) != null) {
                response_string = response_string + line;
            }

        } catch (Exception e) {
            Log.v("TAG","SSL Errror " + e.toString());
        }

        // REMOVE HASH BECAUSE OF NumberFormatException
        for (int index = response_string.indexOf("hash");
             index >= 0;
             index = response_string.indexOf("hash", index + 1))

        {
            String straaa = response_string.substring(index + 7, index + 24);
            response_string = response_string.replaceAll(straaa, "");
            //System.out.println(straaa);
        }


        JSONParser parser=new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response_string);

            JSONArray items =  (JSONArray) jsonObject.get("items");

            for (int i = 0; i < items.size(); i++) {
                JSONObject object = (JSONObject) items.get(i);
                fileidlist.add(object.get("fileid").toString());
                Log.v("TAG","Image id: " + object.get("fileid").toString());
            }


        } catch (Exception e1) {
            e1.printStackTrace();
            try{
                JSONObject jsonObject = (JSONObject) parser.parse(response_string);
                String error = (String) jsonObject.get("error");

                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        client.getConnectionManager().shutdown();

    }

    public Scheme getMockedScheme() throws Exception {
        MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();
        return new Scheme("https", mySSLSocketFactory, 443);
    }

    class MySSLSocketFactory extends SSLSocketFactory {
        javax.net.ssl.SSLSocketFactory socketFactory = null;

        public MySSLSocketFactory(KeyStore truststore) throws Exception {
            super(truststore);
            socketFactory = getSSLSocketFactory();
        }

        public MySSLSocketFactory() throws Exception {
            this(null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException {
            return socketFactory.createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return socketFactory.createSocket();
        }

        javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws Exception {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            return sslContext.getSocketFactory();
        }

    }


    public String response(){
        return response_string;
    }

    public int responseCode(){
        return response_code;
    }

    public ArrayList<String> GetPhotoIds(){
        return fileidlist;
    }
}
