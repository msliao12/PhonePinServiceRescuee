package com.example.phonepinservicerescuee;

import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.FirebaseApp;

import static com.example.phonepinservicerescuee.AppChannel.CHANNEL_2_ID;




public class MainActivity extends AppCompatActivity {


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

        ToggleButton Searching = (ToggleButton) this.findViewById(R.id.toggleButton);
        Searching.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(SOUND, "Successful Toggle");
                    AlarmPlayer.start();

                }else{
                    Log.d(SOUND, "Toggle Off");
                    AlarmPlayer.pause();

                }
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