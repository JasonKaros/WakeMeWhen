package jcubed.wakemewhen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AlarmEdit extends AppCompatActivity {

    private Alarm usrAlarm;
    private final String TAG = "AlarmEdit: ";
    private double latitude;
    private double longitude;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        //getting lat-long doubles from the MapActivity
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(MapsActivity.LATITUTDE,0);
        longitude = intent.getDoubleExtra(MapsActivity.LONGITUDE,0);
        if (intent.hasExtra(MapsActivity.ADDRESS)) {
            address = intent.getStringExtra(MapsActivity.ADDRESS); }
        Log.i(TAG, "Latitude: " + String.valueOf(latitude)
               + ", Longitude: " + String.valueOf(longitude));
    }


}
