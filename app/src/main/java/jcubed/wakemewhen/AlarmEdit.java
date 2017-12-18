package jcubed.wakemewhen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AlarmEdit extends AppCompatActivity {

    private Alarm usrAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
    }
}
