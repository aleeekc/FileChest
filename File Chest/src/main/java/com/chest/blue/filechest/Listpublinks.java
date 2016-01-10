package com.chest.blue.filechest;

/**
 * Created by Blue on 6/6/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


class Listpublinks extends AsyncTask<String, Void, String> {

    TheInterface listener;
    private Exception exception = null;
    public String ResultString = null;
    public int response_code;
    public String response_string;
    Context context;

    public Listpublinks (Context context)
    {
        this.context = context;
    }


    public interface TheInterface {

     //   public void theMethod(String result);

    }

    protected String doInBackground(String... auth) {

        HttpClient client = new DefaultHttpClient();
        try {
            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());
            HttpGet GET = new HttpGet("https://api.pcloud.com/listpublinks?iconformat=id&auth=" + auth[0]);


            Log.v("TAG", "Check if it works !!!");
            Log.v("TAG","Request is: " + "https://api.pcloud.com/listpublinks?iconformat=id&auth=" + auth[0]);
            HttpResponse response = client.execute(GET);
            Log.v("TAG", response.toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            response_code = response.getStatusLine().getStatusCode();

            String line = "";
            while ((line = rd.readLine()) != null) {
                Log.v("TAG",line);
                response_string = response_string + line;
            }



        } catch (Exception e) {
            Log.v("TAG", "SSL Errror " + e.toString());
        }

        return response_string;
    }

    protected void onPostExecute(String stream_url) {
        // TODO: check this.exception
        // TODO: do something with the feed
        if (this.exception != null)
            this.exception.printStackTrace();

        this.ResultString = stream_url;

        //if (listener != null)
        //{
//            listener.theMethod(stream_url);
        //}

        Intent linkIntent = new Intent(context, LinksActivity.class);
        linkIntent.putExtra("response",response_string);
        context.startActivity(linkIntent);

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