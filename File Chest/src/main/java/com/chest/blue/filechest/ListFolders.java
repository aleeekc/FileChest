package com.chest.blue.filechest;

/**
 * Created by Blue on 6/6/2015.
 */

import android.util.Log;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("deprecation")
public class ListFolders {

    String response_string = "";

    public void getFolder(String auth) {

        HttpClient client = new DefaultHttpClient();
        try {

            client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());

            HttpGet GET_folders = new HttpGet("https://api.pcloud.com/listfolder?folderid=0&recursive=1&nofiles=0&auth=" + auth);
            HttpResponse response = client.execute(GET_folders);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                response_string = response_string + line;
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


        } catch (Exception e) {
            Log.v("TAG", "SSL Expection: " + e.toString());
        } finally {
            client.getConnectionManager().shutdown();
        }

    }

    public Scheme getMockedScheme() throws Exception {
        MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();
        return new Scheme("https", mySSLSocketFactory, 443);
    }

    public String foldersList() {
        return response_string;
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