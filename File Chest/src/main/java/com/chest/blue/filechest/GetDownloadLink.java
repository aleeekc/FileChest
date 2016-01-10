package com.chest.blue.filechest;

/**
 * Created by Blue on 7/13/2015.
 */

import android.os.AsyncTask;
import android.text.BoringLayout;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("deprecation")
public class GetDownloadLink extends AsyncTask<String, Void, Boolean>  {
    String fileid;
    String auth;
    String response_string = "";
    String link = "";
    String error = "";

   public GetDownloadLink(String fileid, String auth){
       this.fileid = fileid;
       this.auth = auth;
   }
    @Override
    protected Boolean doInBackground(String... params) {
        Boolean bool = getdownloadlink(fileid, auth);
        return bool;
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    public boolean getdownloadlink(String fileid, String auth){

        HttpClient client = new DefaultHttpClient();
        try {

            HttpGet GET = new HttpGet("https://api.pcloud.com/getfilepublink?" + fileid + "&auth=" + auth);
            Log.v("TAG","https://api.pcloud.com/getfilepublink?" + fileid + "&auth=" + auth);
            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());


            HttpResponse response = client.execute(GET);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


            String line = "";
            while ((line = rd.readLine()) != null) {
                response_string = response_string + line;
            }

        } catch (Exception e) {
            Log.v("TAG", "SSL Errror " + e.toString());
            e.printStackTrace();
            return false;
        }

        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response_string);

            link = (String) jsonObject.get("link");
            if(!link.equals(null))
            link.replaceAll("\\/", "/");

            if (link == null || link.equals("")) {
                error = (String) jsonObject.get("error");
                Log.v("TAG", "The error is: " + error);

                client.getConnectionManager().shutdown();

                return false;
            }

            // System.out.println("The auth is -> " + auth);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        client.getConnectionManager().shutdown();

        return true;
    }

    public String response() {
        return response_string;
    }

    public String link() {
        return link;
    }

    public String Error() {
        return error;
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
