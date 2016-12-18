package com.caco3.orca.data.orioks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class OrioksDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orioks.db";
    private static final int DB_VERSION = 1;


    public OrioksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    /*package*/ static class BaseColumns {
        /** Integer, primary key */
        /*package*/ static final String KEY__ID = "_id";
    }

    /**
     * Teachers table.
     * Stores {@link com.caco3.orca.orioks.model.Teacher} entities
     */
    /*package*/ static class Teachers {
        /*package*/ static final String TABLE_NAME = "teachers";
        /*package*/ static final String KEY_FIRST_NAME = "first_name";
        /*package*/ static final String KEY_LAST_NAME = "last_name";
        /*package*/ static final String KEY_PATRONYMIC = "patronymic";

        private static void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                    + BaseColumns.KEY__ID + " INTEGER PRIMARY KEY, "
                    + KEY_FIRST_NAME + " TEXT, "
                    + KEY_LAST_NAME + " TEXT, "
                    + KEY_PATRONYMIC + " TEXT"
                    + ")");
        }

        private static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Teachers.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Teachers.onUpgrade(db, oldVersion, newVersion);
    }
}
