package jcubed.wakemewhen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class AlarmEdit extends AppCompatActivity {

    private Alarm usrAlarm;
    private final String TAG = "AlarmEdit: ";
    private double latitude;
    private double longitude;
    private String address;
    private DBAdapter db;
    private int ALARM_ID;
    private int REQUEST_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        //get db
        db = new DBAdapter(this);
        db.open();

        Intent intent = getIntent();

        ALARM_ID = intent.getIntExtra("id", -1);
        REQUEST_ID = intent.getIntExtra("req_id", -1);

        //getting lat-long doubles from the MapActivity
        if (REQUEST_ID == 1) {
            latitude = intent.getDoubleExtra(MapsActivity.LATITUTDE, 0);
            longitude = intent.getDoubleExtra(MapsActivity.LONGITUDE, 0);
            if (intent.hasExtra(MapsActivity.ADDRESS)) {
                address = intent.getStringExtra(MapsActivity.ADDRESS);
            }
            Log.d(TAG, "Latitude: " + String.valueOf(latitude)
                    + ", Longitude: " + String.valueOf(longitude));
            Log.d(TAG, "Intent From Map Activity");
        } else if (REQUEST_ID == 2) {
            Button temp = (Button)findViewById(R.id.btn_cancel);
            temp.setText("Discard Changes");
            temp = (Button)findViewById(R.id.btn_confirm);
            Log.d(TAG, "Intent From Main Activity");
        } else {
            Log.d(TAG, "Error Processing Intent");
            //intent back to MainActivity
        }
    }
}
