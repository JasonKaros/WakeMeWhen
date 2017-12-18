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
    Context mContext;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.alarm_lv);

        //Create new table
        db = new DBAdapter(this);
        db.open();

        Alarm a1 = new Alarm("Work", new Double[]{-100.1, 37.5}, 5,
                1, "1 Broadway, NY, NY");
        Alarm a2 = new Alarm("School", new Double[]{-88.1, 32.5}, 2,
                0, "270 Mohegan Ave, New London, CT");

        db.addAlarm(a1);
        db.addAlarm(a2);

        //retrieve alarms
        final ArrayList<Alarm> mAlarmList = db.getAllAlarms();

        //create new adapter and populate with AlarmList
        mAdapter = new AlarmAdapter(this, mAlarmList);
        mListView.setAdapter(mAdapter);

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get user selection
                Alarm selectedAlarm = mAlarmList.get(position);

                // create new intent
                Intent detailIntent = new Intent(context, AlarmEdit.class);

                // put identifying information
                detailIntent.putExtra("alarm_id", id);

                // start activity
                startActivity(detailIntent);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
        }
        return false;
    }
}
