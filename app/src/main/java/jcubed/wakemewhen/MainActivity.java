package jcubed.wakemewhen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/*Jason Karos, Brian Ward, Camden Wagner
* WakeMeWhen
* 12/9/2017
*/

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList mArrayList;
    AlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.alarm_lv);

        //retrieve alarms
        List<Alarm> alarmList = mAdapter.getAlarms();

        //create new adapter and populate with AlarmList
        mAdapter = new AlarmAdapter(this, mArrayList);
        mListView.setAdapter(mAdapter);
    }
}
