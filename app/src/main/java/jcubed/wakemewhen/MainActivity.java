package jcubed.wakemewhen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    Context mContext;
    DBAdapter mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.alarm_lv);
        mDatabase = new DBAdapter(this);

        //retrieve alarms
        final List<Alarm> alarmList = mDatabase.getAllAlarms();

        //create new adapter and populate with AlarmList
        mAdapter = new AlarmAdapter(this, mArrayList);
        mListView.setAdapter(mAdapter);

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get user selection
                Alarm selectedAlarm = alarmList.get(position);

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
 //   @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case
//                Intent intent = new Intent("j.cubed.booklibrary.BookActivity");
//                intent.putExtra("id", -2);
//                startActivityForResult(intent, BOOK_RECORD_REQUEST);
//        }
//        return false;
//    }
}
