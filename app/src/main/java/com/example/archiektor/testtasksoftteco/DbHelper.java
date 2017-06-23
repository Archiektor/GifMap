package com.example.archiektor.testtasksoftteco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.archiektor.testtasksoftteco.DBScheme.Cols.*;
import static com.example.archiektor.testtasksoftteco.DBScheme.DbTable.*;

/**
 * Created by Archiektor on 23.06.2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    //String addColumn = "ALTER TABLE " + DrinksTable.TABLE_NAME + " ADD COLUMN FAVORITE NUMERIC;";
    private static final String DATABASE_DELETE = "DROP TABLE " + TABLE_NAME;
    //String renameTable = "ALTER TABLE DRINK RENAME TO FOO;";

    private static final String DB_NAME = "softtecoDb.db";

    private static final int DB_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    //private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + TEXT_TYPE + COMMA_SEP
            + EMAIL + TEXT_TYPE + COMMA_SEP
            + WEBSITE + TEXT_TYPE + COMMA_SEP
            + PHONE + TEXT_TYPE + COMMA_SEP
            + CITY + TEXT_TYPE + ")";

    DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    static void insertData(SQLiteDatabase db, String name, String email, String website, String phone, String city) {

        ContentValues contactValues = new ContentValues();

        contactValues.put(NAME, name);
        contactValues.put(EMAIL, email);
        contactValues.put(WEBSITE, website);
        contactValues.put(PHONE, phone);
        contactValues.put(CITY, city);

        db.insert(DBScheme.DbTable.TABLE_NAME, null, contactValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {


            db.execSQL(DATABASE_CREATE);

//            insertData(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
//            insertData(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam",
//                    R.drawable.cappuccino);
//            insertData(db, "Filter", "Our best drip coffee", R.drawable.filter);
        }

        if (oldVersion < 2) {
            //db.execSQL(DATABASE_DELETE);
        }
    }
}
