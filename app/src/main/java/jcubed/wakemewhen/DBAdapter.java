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
    private static final String KEY_AUTHOR = "author";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "books";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE +
            " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TITLE + " text not null, " + KEY_AUTHOR + " text not null);";

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
        initialValues.put(KEY_TITLE, book.getTitle());
        initialValues.put(KEY_AUTHOR, book.getAuthor());
        long q = db.insert(DATABASE_TABLE, null, initialValues);  //Return new rowID or -1
    }

    //---deletes a particular contact by its rowID---
    public void deleteBook(Book book) {
        db.delete(DATABASE_TABLE, KEY_ROWID + "=" + book.getID(), null);
    }


    //---retrieves all the contacts---
    public LinkedList<Book> getAllBooks() {
        Cursor books = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_AUTHOR}, null, null, null, null, null);
        books.moveToFirst();
        LinkedList bookList = new LinkedList();

        while (!books.isAfterLast()){
            bookList.add(new Book(books.getInt(0), books.getString(1), books.getString(2)));
            books.moveToNext();
        }
        return bookList;
    }
    //---retrieves a particular book---
    public Book searchBook(int id) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_AUTHOR}, KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        mCursor.moveToFirst();
        Book book = new Book(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2));
        mCursor.close();
        return book;
    }

    //---updates a contact---
    public void updateBook(int id, String title, String author) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_AUTHOR, author);
        db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
    }

    public void dropTable(){
        DBHelper.close();
        context.deleteDatabase(DATABASE_NAME);
    }
}

