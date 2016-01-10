package com.chest.blue.filechest;

/**
 * Created by Blue on 7/12/2015.
 */

import android.content.Context;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("deprecation")
public class Register {

    String response_string = "";
    String auth = "";
    int response_code;
    String error = "";


    public boolean RegisterSuccess(Context context, String email, String password) {

        HttpClient client = new DefaultHttpClient();
        try {
            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());
            HttpPost POST = new HttpPost("https://api.pcloud.com/register?termsaccepted=yes&mail=" + email + "&password=" + password + "&os=4&device=&ref=0");

            Log.v("TAG","Register request: " + "https://api.pcloud.com/register?termsaccepted=yes&mail=" + email + "&password=&" + password + "&os=4&device=&ref=0");

            HttpResponse response = client.execute(POST);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            response_code = response.getStatusLine().getStatusCode();
            String line = "";
            while ((line = rd.readLine()) != null) {
                response_string = response_string + line;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("TAG","Error in the register request!");
            return false;
        }

        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response_string);

            auth = (String) jsonObject.get("auth");

            // System.out.println("The auth is -> " + auth);

            Log.v("", "The auth is ->>" + auth);

            // Crash when trying to register twice
            if (auth == null || auth.equals("")) {
                error = (String) jsonObject.get("error");
                Log.v("TAG","The error is: " + error);

                client.getConnectionManager().shutdown();
                return false;
            }

        } catch (Exception e) {
            Log.v("TAG","Error in the parsing of the register response!");
            e.printStackTrace();
            return false;
        }

        ListFolders listFoldObj = new ListFolders();
        listFoldObj.getFolder(auth);
        Log.v("TAG", "The folders were listed");

        DBHelper dbHelpObj = new DBHelper(context);
        Log.v("TAG", "DBHelper object was created");
        dbHelpObj.setAuth(auth);

        client.getConnectionManager().shutdown();
        return true;
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

    public String error() {
        return error;
    }

    public String auth() {
        return auth;
    }

    public String response() {
        return response_string;
    }

    public int responseCode() {
        return response_code;
    }
}
