package jcubed.wakemewhen;

import android.content.Intent;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AlarmEdit extends AppCompatActivity {

    private Alarm usrAlarm;
    private final String TAG = "AlarmEdit";
    private double latitude;
    private double longitude;
    private String address;
    private DBAdapter db;
    private int ALARM_ID;
    private int REQUEST_ID;
    private Alarm USR_ALARM;
    private SeekBar dBar;
    private Intent intent;
    private EditText et;
    private CheckBox vChk;
    private Boolean isAddrEditing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);


        //get db
        db = new DBAdapter(this);
        db.open();

        dBar = (SeekBar)findViewById(R.id.distance_slider);
        vChk = findViewById(R.id.vibrate_chk);
        et = (EditText)findViewById(R.id.alarmName_et);
        final TextView usrD = (TextView)findViewById(R.id.usrDistance_tv);

        intent = getIntent();

        REQUEST_ID = intent.getIntExtra("req_id", -1);

        //We are creating a new alarm
        if (REQUEST_ID == 1) {
            Log.d(TAG, "Intent From AddBook");
            //get location and address from MapActivity intent
            latitude = intent.getDoubleExtra(MapsActivity.LATITUTDE, 0);
            longitude = intent.getDoubleExtra(MapsActivity.LONGITUDE, 0);
            if (intent.hasExtra(MapsActivity.ADDRESS)) {
                address = intent.getStringExtra(MapsActivity.ADDRESS);
            } else{ address = "Location exists, partial address not displayable";}
            Log.d(TAG, "Latitude: " + String.valueOf(latitude)
                    + ", Longitude: " + String.valueOf(longitude));
            Button temp = findViewById(R.id.addr_btn);
            temp.setText(address);
        }
        //we are editing an existing alarm
        else if (REQUEST_ID == 2) {
            Log.d(TAG, "Intent From Main Activity");

            ALARM_ID = intent.getIntExtra("id", -1);
            Log.d("Alarm ID", Integer.toString(ALARM_ID));
            USR_ALARM = db.searchAlarm(ALARM_ID);

            //handle buttons
            Button temp = (Button) findViewById(R.id.btn_cancel);
            temp.setText("Discard Changes");
            temp = (Button) findViewById(R.id.btn_delete);
            temp.setVisibility(View.VISIBLE);
            temp = findViewById(R.id.addr_btn);
            temp.setText(USR_ALARM.getAddress());

            //handle slider
            Log.d(TAG, Integer.toString(USR_ALARM.getDistance()));
            dBar.setProgress(USR_ALARM.getDistance());
            usrD.setText(Integer.toString(USR_ALARM.getDistance()));

            //handle edit text
            et.setText(USR_ALARM.getName());

            //handle check box
            vChk.setChecked(USR_ALARM.isVibrate() == 1);
        }
        //we are editing the slected address of an either new or existing alarm
        else if (REQUEST_ID == 3){
            Log.d(TAG, "Intent From Edit Address");
            latitude = intent.getDoubleExtra(MapsActivity.LATITUTDE, 0);
            longitude = intent.getDoubleExtra(MapsActivity.LONGITUDE, 0);
            if (intent.hasExtra(MapsActivity.ADDRESS)) {
                address = intent.getStringExtra(MapsActivity.ADDRESS);
            } else{ address = "Location exists, partial address not displayable";}
            Log.d(TAG, "Latitude: " + String.valueOf(latitude)
                    + ", Longitude: " + String.valueOf(longitude));

            ALARM_ID = intent.getIntExtra("id", -1);
            Log.d("Alarm ID", Integer.toString(ALARM_ID));
            USR_ALARM = db.searchAlarm(ALARM_ID);

            //handle buttons
            Button temp = (Button) findViewById(R.id.btn_cancel);
            temp.setText("Discard Changes");
            temp = (Button) findViewById(R.id.btn_delete);
            temp.setVisibility(View.VISIBLE);
            temp = findViewById(R.id.addr_btn);
            temp.setText(address);

            //handle slider
            dBar.setProgress(USR_ALARM.getDistance());
            usrD.setText(Integer.toString(USR_ALARM.getDistance()));

            //handle edit text
            et.setText(USR_ALARM.getName());

            //handle check box
            vChk.setChecked(USR_ALARM.isVibrate() == 1);
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
        if (REQUEST_ID == 1){
            Toast.makeText(this, "Alarm Discarded", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Changes Discarded", Toast.LENGTH_SHORT).show();
        }

        db.close();

        Intent i = new Intent(this, MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
    }
    public void BtnAddrClicked(View view){

        Intent i = new Intent(this, MapsActivity.class);
        Log.d(TAG, "REQ ID FOR BTNADDR"+Integer.toString(REQUEST_ID));

        if (REQUEST_ID == 2) {
            i.putExtra("id", USR_ALARM.getId());
            REQUEST_ID = 3;
        }

        i.putExtra("req_id", REQUEST_ID);
        startActivity(i);
    }
    public void BtnSaveClicked(View view){
        //create new lat_lon array
        Double[] lat_lon =  new Double[2];
        lat_lon[0] = latitude;
        lat_lon[1]= longitude;
        //get vibrate flag
        int vFlag = 0;
        if (vChk.isChecked()){
            vFlag = 1;
        }

        if (REQUEST_ID == 1 || REQUEST_ID == 4) {
            Alarm newAlarm = new Alarm(et.getText().toString(), lat_lon, dBar.getProgress(), vFlag, 1, address);
            db.addAlarm(newAlarm);
            Toast.makeText(this, "'"+newAlarm.getName()+"' Created", Toast.LENGTH_SHORT).show();
        } else if (REQUEST_ID == 2 || REQUEST_ID == 3){
            if (REQUEST_ID == 3){
                USR_ALARM.setAddress(address);
                USR_ALARM.setLocation(lat_lon);
                Log.d(TAG, USR_ALARM.getLocation().toString());
            }
            Log.d(TAG, Integer.toString(dBar.getProgress()));
            USR_ALARM.setDistance(dBar.getProgress());
            USR_ALARM.setVibrate(vFlag);
            USR_ALARM.setName(et.getText().toString());
            db.updateAlarm(USR_ALARM);
            Toast.makeText(this, "'"+USR_ALARM.getName()+"' Updated", Toast.LENGTH_SHORT).show();
        }
        db.close();

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    public void BtnDeleteClicked(View view){
        db.deleteAlarm(USR_ALARM);
        db.close();

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
