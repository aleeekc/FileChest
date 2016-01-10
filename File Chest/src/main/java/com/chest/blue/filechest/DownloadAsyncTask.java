package com.chest.blue.filechest;

/**
 * Created by Elysi on 8/9/2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//http://stackoverflow.com/questions/18870065/retrieving-a-returned-string-from-asynctask-in-android

class DownloadAsyncTask extends AsyncTask<String[], Void, String> {

    TheInterface listener;
    private Exception exception = null;
    public String ResultString = null;
    public int response_code;
    public String response_string;
    public String URL = "";

    public DownloadAsyncTask (Context context)
    {
        listener = (TheInterface) context;
    }

    public interface TheInterface {

        public void theMethod(String result);

    }

    protected String doInBackground(String[]... arr) {

        String fileid = arr[0].toString();
        String auth = arr[1].toString();

        HttpClient client = new DefaultHttpClient();
        try {
            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());
            HttpGet GET = new HttpGet("https://api.pcloud.com/getfilelink?fileid=" + fileid + "&forcedownload=1&auth=" + auth);


            Log.v("TAG", "Check if it works !!!");
            HttpResponse response = client.execute(GET);
            Log.v("TAG", response.toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            response_code = response.getStatusLine().getStatusCode();

            String line = "";
            while ((line = rd.readLine()) != null) {
                response_string = response_string + line;
            }

        } catch (Exception e) {
            Log.v("TAG", "SSL Errror " + e.toString());
        }

        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response_string);

            String path = jsonObject.get("path").toString();
            path = path.replaceAll("\\/", "/");
            Log.v("TAG", "Path: " + path);

            JSONArray host = (JSONArray) jsonObject.get("hosts");
            Log.v("TAG", "host: " + host.toString());

            URL = (String) host.get(0);

            URL = URL + path;
            Log.v("TAG", "URL: " + URL);


        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(response_string);
                String error = (String) jsonObject.get("error");

                return "";

                //Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        client.getConnectionManager().shutdown();

        return URL;
    }

    protected void onPostExecute(String stream_url) {
        // TODO: check this.exception
        // TODO: do something with the feed
        if (this.exception != null)
            this.exception.printStackTrace();

        this.ResultString = stream_url;

        if (listener != null)
        {
            listener.theMethod(stream_url);
        }
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
}
