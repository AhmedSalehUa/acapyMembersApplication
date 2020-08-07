package com.acpay.acapymembers.Order;

import android.util.Log;

import com.acpay.acapymembers.LocationProvider.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class OrderUtilies {
    private static final String LOG_TAG = OrderUtilies.class.getName();

    public OrderUtilies() {

    }

    public static List<Order> fetchData(String url) {
        URL urlR = getUrl(url);
        String jasonResponse = null;
        try {
            jasonResponse = getHttpRequest(urlR);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Order> books = extractFeuterFromJason(jasonResponse);
        return books;
    }

    public static List<Order> extractFeuterFromJason(String jason) {

        final List<Order> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                Order order = new Order(jsonArrayId.getString("order_num"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("time"),
                        jsonArrayId.getString("place"),
                        jsonArrayId.getString("location"),
                        jsonArrayId.getString("fixType"),
                        jsonArrayId.getString("num_of_matter"),
                        jsonArrayId.getString("dliverCost"),
                        jsonArrayId.getString("notes"),
                        jsonArrayId.getString("files")
                );
                list.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
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
