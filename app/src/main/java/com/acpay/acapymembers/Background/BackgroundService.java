package com.acpay.acapymembers.Background;

import static com.acpay.acapymembers.Background.App.CHANNEL_ID;
import static com.acpay.acapymembers.MainActivity.getAPIHEADER;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.acpay.acapymembers.MainActivity;
import com.acpay.acapymembers.Order.Order;
import com.acpay.acapymembers.Order.OrderUtilies;
import com.acpay.acapymembers.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BackgroundService extends Service implements LocationListener {
    private FirebaseRemoteConfig mRemoteConfig;

    private boolean getLocationOnOf = false;
    private String locationState = "setLocationOnOff";

    private String startTime = "08:00";
    private String startTimeLocation = "setStartTimeLocation";


    private String endTime = "20:00";
    private String endTimeLocation = "setEndTimeLocation";

    Date startingTime;
    Date endingTime;
    Date currentTime;

    boolean statu = false;

    String user_id;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;


    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long MIN_TIME_BW_UPDATES = 10;

    protected LocationManager locationManager;
    FirebaseUser user;

    @Override
    public void onCreate() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        getLocation();
        firebaseRemoteConfig();
        fetchConfig();
<<<<<<< HEAD
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("messages").child(user.getDisplayName());
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message.getName() == user.getDisplayName()) {
=======
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a

        super.onCreate();
    }

    private void notifyME(String sender, String message) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(BackgroundService.this, "0")
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(sender)
                        .setContentText(message)
                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("0",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void firebaseRemoteConfig() {
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mRemoteConfig.setConfigSettingsAsync(configSettings);
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(locationState, getLocationOnOf);
        defaultConfigMap.put(startTimeLocation, startTime);
        defaultConfigMap.put(endTimeLocation, endTime);
        mRemoteConfig.setDefaultsAsync(defaultConfigMap);
    }

    private void fetchConfig() {
        mRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            applyLocationStatue();
                            statu = true;
                        } else {
                            statu = true;
                        }
                    }
                });
    }

    private boolean applyLocationStatue() {
        Log.w("config", mRemoteConfig.getBoolean(locationState) + " / " + mRemoteConfig.getString(startTimeLocation) + " / " + mRemoteConfig.getString(endTimeLocation));
        getLocationOnOf = mRemoteConfig.getBoolean(locationState);
        startTime = mRemoteConfig.getString(startTimeLocation);
        endTime = mRemoteConfig.getString(endTimeLocation);
        return statu;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        try {
//            currentTime = new SimpleDateFormat("hh:mm a").parse(new SimpleDateFormat("hh:mm a").format(new Date()));
//            startingTime = new SimpleDateFormat("hh:mm").parse(startTime);
//            endingTime = new SimpleDateFormat("hh:mm").parse(endTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Acapy Members let this app running in background")
                .setContentText("")
                .setContentText(input)
                .setSmallIcon(R.drawable.appbackground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        getLocation();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getLocation();
                boolean net = checkNetwork();
                boolean gps = checkGps();
                if (longitude == 0.0 || latitude == 0.0 || !net || !gps) {
                    getLocation();
                    if (!gps) {
                         getLocation();
                    }
                    if (!net) {
                        ConnectivityManager cm =
                                (ConnectivityManager) BackgroundService.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (isConnected) {
                            getLocation();
                        } else {
                             getLocation();
                        }
                    }
                } else {
                    // Toast.makeText(BackgroundService.this, "current Location: " + latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                handler.postDelayed(this, 1000 * 5);
            }
        };
        handler.post(runnable);
        return START_REDELIVER_INTENT;
    }

    private boolean checkNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) BackgroundService.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean checkGps() {
        locationManager = (LocationManager) BackgroundService.this.getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isGPSEnabled;
    }

    public Location getLocation() {
        this.canGetLocation = false;
        try {
            locationManager = (LocationManager) BackgroundService.this.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled || !isNetworkEnabled) {


            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission Required", Toast.LENGTH_SHORT).show();
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

        return location;
    }

    @Override
    public void onDestroy() {
        getLocation();
        stopForeground(1);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String api =  getAPIHEADER(BackgroundService.this)+"/locationInserter.php?id=" + user.getUid()
                + "&latitude=" + latitude + "&longlatitude=" + longitude + "&date=" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(new Date());
        RequestQueue queue = Volley.newRequestQueue(BackgroundService.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);

    }
}
