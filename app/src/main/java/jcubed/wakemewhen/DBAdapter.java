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
    private static final String KEY_LOCATION = "address";
    private static final String KEY_DISTANCE = "distance";
    private static final int KEY_V_FLAG = 0;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "WakeMeWhen";
    private static final String DATABASE_TABLE = "alarms";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE +
            " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TITLE + " text not null, " + KEY_DISTANCE + " text not null, " + KEY_V_FLAG + " text not null, "+ KEY_LOCATION + " text not null);";

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
        initialValues.put(KEY_LOCATION, alarm.getAddress());
        long q = db.insert(DATABASE_TABLE, null, initialValues);  //Return new rowID or -1
    }

    //---deletes a particular contact by its rowID---
    public void deleteAlarm(Alarm alarm) {
        db.delete(DATABASE_TABLE, KEY_ROWID + "=" + alarm.getId(), null);
    }


    //---retrieves all the contacts---
    public LinkedList<Alarm> getAllAlarms() {
        Cursor alarms = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_LOCATION}, null, null, null, null, null);
        alarms.moveToFirst();
        LinkedList alarmList = new LinkedList();

        while (!alarms.isAfterLast()){
            String s = alarms.getString(4);
            Double[] doub = new Double[2];
            doub[0] = Double.parseDouble(s.substring(6,17));
            doub[1] = Double.parseDouble(s.substring(23));
            alarmList.add(new Alarm(alarms.getString(1), doub, alarms.getString(2), alarms.getInt(3));
            alarms.moveToNext();
        }
        return alarmList;
    }
    //---retrieves a particular book---
    public Alarm searchAlarm(int id) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_LOCATION}, KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        mCursor.moveToFirst();
        String s = mCursor.getString(4);
        Double[] doub = new Double[2];
        doub[0] = Double.parseDouble(s.substring(6,17));
        doub[1] = Double.parseDouble(s.substring(23));
        Alarm alarm = new Alarm(mCursor.getString(1), doub ,mCursor.getString(2), mCursor.getWantsAllOnMoveCalls(3));
        mCursor.close();
        return alarm;
    }

    //---updates a contact---
    public void updateAlarm(int id, String title, String location) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_LOCATION, location);
        db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
    }

    public void dropTable(){
        DBHelper.close();
        context.deleteDatabase(DATABASE_NAME);
    }
}

