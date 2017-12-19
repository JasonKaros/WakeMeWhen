package jcubed.wakemewhen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

/*Jason Karos, Brian Ward, Camden Wagner
* WakeMeWhen
* 12/9/2017
*/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity: ";
    ListView mListView;
    ArrayList<Alarm> mAlarmList;
    AlarmAdapter mAdapter;
    DBAdapter db;
    int EDIT_ALARM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.alarm_lv);

        //Create new table
        db = new DBAdapter(this);
        db.open();

//        Double[] lat_long = new Double[2];
//        lat_long[0] = 2.222;
//        lat_long[1] = -7.66;
//        Alarm alarm1 = new Alarm("Alarm1", lat_long, 6, 1, 0, "102 Grove Ave, Leominster MA, 01453");
//        Alarm alarm2 = new Alarm("Alarm2", lat_long, 6, 1, 0, "222 Grove Ave, Leominster MA, 01453");
//        db.addAlarm(alarm1);
//        db.addAlarm(alarm2);

        //retrieve alarms
        mAlarmList = db.getAllAlarms();

        //create new adapter and populate with AlarmList
        mAdapter = new AlarmAdapter(this, mAlarmList);
        mListView.setAdapter(mAdapter);

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 1
                Alarm usr_Alarm = mAlarmList.get(position);

                // 2
                Intent detailIntent = new Intent(context, AlarmEdit.class);

                // 3
                detailIntent.putExtra("id", usr_Alarm.getId());

                // 4
                startActivityForResult(detailIntent, EDIT_ALARM_REQUEST);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override

    //opens mapsactivity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
        }
        return false;
    }
}
