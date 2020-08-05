package com.acpay.acapymembers;

import android.os.AsyncTask;
import android.util.Log;

import com.acpay.acapymembers.LocationProvider.HttpsTrustManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

public class JasonReponser extends  AsyncTask<String, Void, String> {
    private static final String LOG_TAG = JasonReponser.class.getName();
     private static String userId;
    private static boolean finish=false;

    public static boolean isFinish() {
        return finish;
    }

    public static void setFinish(boolean finish) {
        JasonReponser.finish = finish;
    }

    public static String getUserId() {
        return userId;
    }



    public JasonReponser() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        return fetchData(strings[0]);
    }

    public static String fetchData(String location) {
        URL urlR = getUrl(location);
        String jasonResponse = null;
        try {
            jasonResponse = getHttpRequest(urlR);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        Log.e(LOG_TAG, jasonResponse);
        userId=jasonResponse;
        finish=true;
        return jasonResponse;
    }

    private static URL getUrl(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String getHttpRequest(URL url) throws IOException {
        String jasonRespons = "";
        if (url == null) {
            return jasonRespons;
        }

        HttpsTrustManager.allowAllSSL();
        HttpURLConnection httpURLConnection = null;

        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(45000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            HttpsTrustManager.allowAllSSL();
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jasonRespons = getStringFromInpurStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jasonRespons;
    }

    private static String getStringFromInpurStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
