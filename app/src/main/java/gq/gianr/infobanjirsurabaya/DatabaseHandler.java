package gq.gianr.infobanjirsurabaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import gq.gianr.infobanjirsurabaya.model.Notif;

/**
 * Created by j on 18/07/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Notifications";

    // Notifs table name
    private static final String TABLE_NOTIF = "notif";

    // Notifs Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TGL = "tanggal";
    private static final String KEY_NOW = "tanggal_real";
    private static final String KEY_CHJ = "curah_hujan";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NotifS_TABLE = "CREATE TABLE " + TABLE_NOTIF + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TGL + " TEXT,"
                + KEY_NOW + " TEXT," + KEY_CHJ + " TEXT" + ")";
        db.execSQL(CREATE_NotifS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIF);

        // Create tables again
        onCreate(db);
    }

    // Adding new Notif
    public void addNotif(Notif notif) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TGL, notif.get_tgl()); // Contact Name
        values.put(KEY_NOW, notif.get_tgl()); // Contact Name
        values.put(KEY_CHJ, notif.get_chj()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_NOTIF, null, values);
        db.close();
    }

    // Getting single Notif
    public Notif getNotif(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIF, new String[] { KEY_ID,
                        KEY_TGL, KEY_NOW, KEY_CHJ }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return contact
        return new Notif(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(3), cursor.getString(2));
    }

    // Getting All Notifs
    public List<Notif> getAllNotifs() {
        List<Notif> notifs = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIF;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notif notif = new Notif();
                notif.set_id(Integer.parseInt(cursor.getString(0)));
                notif.set_tgl(cursor.getString(1));
                notif.set_now(cursor.getString(2));
                notif.set_chj(cursor.getString(3));
                // Adding contact to list
                notifs.add(notif);
            } while (cursor.moveToNext());
        }

        // return contact list
        return notifs;
    }

    // Getting Notifs Count
    public int getNotifsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTIF;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single Notif
    public int updateNotif(Notif notif) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TGL, notif.get_tgl());
        values.put(KEY_CHJ, notif.get_chj());
        values.put(KEY_NOW, notif.get_now());

        // updating row
        return db.update(TABLE_NOTIF, values, KEY_ID + " = ?",
                new String[] { String.valueOf(notif.get_id()) });
    }

    // Deleting single Notif
    public void deleteNotif(Notif notif) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIF, KEY_ID + " = ?",
                new String[] { String.valueOf(notif.get_id()) });
        db.close();
    }
}
