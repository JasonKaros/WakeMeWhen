package jcubed.wakemewhen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBAdapter {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_V_FLAG = "vflag";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "WakeMeWhen";
    private static final String DATABASE_TABLE = "alarms";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE +
            " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TITLE + " text not null, " + KEY_DISTANCE + " integer not null, " + KEY_V_FLAG
            + " integer not null, "+ KEY_ADDRESS + " text not null, "
            + KEY_LATITUDE + " real not null, " + KEY_LONGITUDE + " real not null" + ");";

    //Create table with columns: ROWID, TITLE, DISTANCE, V_FLAG, ADDRESS, LATITUDE and LONGITUDE----

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //Called when no database exists in disk and the helper class
        //needs to create one
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /* Called when there is a database version mismatch meaning the version of the database
        on disk needs to be upgraded to the current version. */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
            onCreate(db);
        }
    }
    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public void addAlarm(Alarm alarm) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, alarm.getName());
        initialValues.put(KEY_ADDRESS, alarm.getAddress());
        long q = db.insert(DATABASE_TABLE, null, initialValues);  //Return new rowID or -1
    }

    //---deletes a particular contact by its rowID---
    public void deleteAlarm(Alarm alarm) {
        db.delete(DATABASE_TABLE, KEY_ROWID + "=" + alarm.getId(), null);
    }


    //---retrieves all the alarms in DB---
    public LinkedList<Alarm> getAllAlarms() {
        // query for alarms w/ columns: id, name, lat, long, distance, vflag, address
        Cursor alarms = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_LATITUDE, KEY_LONGITUDE, KEY_DISTANCE, KEY_V_FLAG, KEY_ADDRESS},
                null, null, null, null, null);
        alarms.moveToFirst();
        LinkedList alarmList = new LinkedList();

        while (!alarms.isAfterLast()){
            double lat = alarms.getDouble(2);
            double lon = alarms.getDouble(3);
            Double[] latLonArray = new Double[]{lat, lon};
            alarmList.add(new Alarm(alarms.getString(1), latLonArray, alarms.getInt(4), alarms.getInt(5),
                    alarms.getString(6)));
            alarms.moveToNext();
        }
        return alarmList;
    }
    //---retrieves a particular alarm by id---
    public Alarm searchAlarm(int id) throws SQLException {
        Cursor alarmCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                        KEY_LATITUDE, KEY_LONGITUDE, KEY_DISTANCE, KEY_V_FLAG, KEY_ADDRESS},
                        KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        alarmCursor.moveToFirst();
        double lat = alarmCursor.getDouble(2);
        double lon = alarmCursor.getDouble(3);
        Double[] latLonArray = new Double[] {2,3};
        Alarm alarm = new Alarm(alarmCursor.getString(1), latLonArray, alarmCursor.getInt(4),
                alarmCursor.getInt(5),alarmCursor.getString(6));
        alarmCursor.close();
        return alarm;
    }

    //---updates an alarm---
    public void updateAlarm(int id, String title, String location) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_ADDRESS, location);
        db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
    }

    public void dropTable(){
        DBHelper.close();
        context.deleteDatabase(DATABASE_NAME);
    }
}

