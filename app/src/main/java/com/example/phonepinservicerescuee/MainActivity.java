package com.example.phonepinservicerescuee;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.phonepinservicerescuee.AppChannel.CHANNEL_2_ID;




public class MainActivity extends AppCompatActivity {

    public static final String LONG = "Long";
    public static final String LAT = "Lat";
    public static final String TAG1 = "Saving Location Data";
    private FirebaseFirestore mCollect = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient client;

    public static final String SOUND = "sound";


    private TextView mTextMessage;
    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;






    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        final MediaPlayer AlarmPlayer = MediaPlayer.create(this, R.raw.alarm);
        AlarmPlayer.setLooping(true);
        client = LocationServices.getFusedLocationProviderClient(this);


        ToggleButton Searching = (ToggleButton) this.findViewById(R.id.toggleButton);
        Searching.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(SOUND, "Successful Toggle");
                    getLocation();
                    AlarmPlayer.start();

                }else{
                    Log.d(SOUND, "Toggle Off");
                    AlarmPlayer.pause();

                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.showOverflowMenu();
        myToolbar.setTitleTextColor(R.color.colorMain);

    }

    public void getLocation(){
        MapsActivity ma = new MapsActivity();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        setLocation(location.getLongitude(),location.getLatitude());
                    }
                }
            });
        }
        else{
            ma.requestLocationPermission();

        }


    }

    public void setLocation(double lon, double lat){
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(LONG, lon);
        dataToSave.put(LAT, lat);
        mCollect.collection("GeoData")
                .add(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG1, "Document Saved Successful" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG1, "Error Adding Document" + e.toString());
                    }
                });
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_about:
                    mTextMessage.setText(R.string.about);
                    return true;
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;

        }
    };


    public void goToMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    public void goToLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void sendOnChannel2 (View v){
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();
        //String title = "Priority 2";
        //String message = "Message 2";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);

    }


};