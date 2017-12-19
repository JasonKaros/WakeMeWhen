package jcubed.wakemewhen;

import android.content.Intent;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AlarmEdit extends AppCompatActivity {

    private Alarm usrAlarm;
    private final String TAG = "AlarmEdit: ";
    private double latitude;
    private double longitude;
    private String address;
    private DBAdapter db;
    private int ALARM_ID;
    private int REQUEST_ID;
    private Alarm USR_ALARM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);


        //get db
        db = new DBAdapter(this);
        db.open();

        SeekBar dBar = (SeekBar)findViewById(R.id.distance_slider);
        final TextView usrD = (TextView)findViewById(R.id.usrDistance_tv);

        Intent intent = getIntent();

        REQUEST_ID = intent.getIntExtra("req_id", -1);

        REQUEST_ID = 1;

        //getting lat-long doubles from the MapActivity
        if (REQUEST_ID == 1) {
            Log.d(TAG, "Intent From Map Activity");
            latitude = intent.getDoubleExtra(MapsActivity.LATITUTDE, 0);
            longitude = intent.getDoubleExtra(MapsActivity.LONGITUDE, 0);
            if (intent.hasExtra(MapsActivity.ADDRESS)) {
                address = intent.getStringExtra(MapsActivity.ADDRESS);
            }
            Log.d(TAG, "Latitude: " + String.valueOf(latitude)
                    + ", Longitude: " + String.valueOf(longitude));
        } else if (REQUEST_ID == 2) {
            Log.d(TAG, "Intent From Main Activity");

            ALARM_ID = intent.getIntExtra("id", -1);
            USR_ALARM = db.searchAlarm(ALARM_ID);

            //handle buttons
            Button temp = (Button)findViewById(R.id.btn_cancel);
            temp.setText("Discard Changes");
            temp = (Button)findViewById(R.id.btn_delete);
            temp.setVisibility(View.VISIBLE);

            //handle slider
            dBar.setProgress(USR_ALARM.getDistance());
            usrD.setText(Integer.toString(USR_ALARM.getDistance()));


        } else {
            Log.d(TAG, "Error Processing Intent");
            //intent back to MainActivity
        }

        dBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String iStr = Integer.toString(i);
                usrD.setText(iStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void BtnCancelClicked(View view){

    }
}
