package com.caco3.orca.data.schedule;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*package*/final class ScheduleDbHelper extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "schedule.db";
    private static final int DB_VERSION = 1;

    /*package*/ ScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ScheduleItems.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ScheduleItems.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * @see {@link com.caco3.orca.scheduleapi.ScheduleItem}
     */
    /*package*/ static class ScheduleItems {
        /*package*/ static final String TABLE_NAME = "schedule_items";

        /*package*/ static final String KEY__ID = "_id";
        /*package*/ static final String KEY_DAY_OF_WEEK = "day_of_week";
        /*package*/ static final String KEY_ORDER_IN_DAY = "order_in_day";
        /*package*/ static final String KEY_REPEATS_EVERY_FIRST_WEEK_OF_MONTH
                = "repeats_every_first_week_of_month";
        /*package*/ static final String KEY_REPEATS_EVERY_SECOND_WEEK_OF_MONTH
                = "repeats_every_second_week_of_month";
        /*package*/ static final String KEY_REPEATS_EVERY_THIRD_WEEK_OF_MONTH
                = "repeats_every_third_week_of_month";
        /*package*/ static final String KEY_REPEATS_EVERY_FOURTH_WEEK_OF_MONTH
                = "repeats_every_fourth_week_of_month";
        /*package*/ static final String KEY_DISCIPLINE_NAME = "disc_name";
        /*package*/ static final String KEY_IS_LECTURE = "is_lecture";
        /*package*/ static final String KEY_IS_SEMINAR = "is_seminar";
        /*package*/ static final String KEY_IS_LABORATORY_WORK = "is_laboratory_work";
        /*package*/ static final String KEY_CLASSROOM = "classroom";
        /*package*/ static final String KEY_TEACHER_NAME = "teacher_name";
        /*package*/ static final String KEY_IS_PHYSICAL_EDUCATION = "is_pe";
        /*package*/ static final String KEY_IS_MILITARY_LESSON = "is_military";
        /*package*/ static final String KEY_GROUP_NAME = "group_name";

        private static void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY__ID + " INTEGER PRIMARY KEY, "
                    + KEY_DAY_OF_WEEK + " INTEGER, "
                    + KEY_ORDER_IN_DAY + " INTEGER, "
                    + KEY_REPEATS_EVERY_FIRST_WEEK_OF_MONTH + " INTEGER, "
                    + KEY_REPEATS_EVERY_SECOND_WEEK_OF_MONTH + " INTEGER, "
                    + KEY_REPEATS_EVERY_THIRD_WEEK_OF_MONTH + " INTEGER, "
                    + KEY_REPEATS_EVERY_FOURTH_WEEK_OF_MONTH + " INTEGER, "
                    + KEY_DISCIPLINE_NAME + " TEXT, "
                    + KEY_IS_LECTURE + " INTEGER, "
                    + KEY_IS_SEMINAR + " INTEGER, "
                    + KEY_IS_LABORATORY_WORK + " INTEGER, "
                    + KEY_CLASSROOM + " TEXT, "
                    + KEY_TEACHER_NAME + " TEXT, "
                    + KEY_IS_PHYSICAL_EDUCATION + " INTEGER, "
                    + KEY_IS_MILITARY_LESSON + " INTEGER, "
                    + KEY_GROUP_NAME + " TEXT"
                    + ");");

        }

        private static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME + " IF EXISTS;");
            onCreate(db);
        }

    }

    /*package*/ static class Groups {
        /*package*/ static final String TABLE_NAME = "groups";
        /**Integer, primary key*/
        /*package*/ static final String KEY__ID = "_id";
        /**Text*/
        /*package*/ static final String KEY_GROUP_NAME = "name";
        private static void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY__ID + " INTEGER PRIMARY KEY, "
                    + KEY_GROUP_NAME + " TEXT"
                    + ")");
        }

        private static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME + " IF EXISTS;");
            onCreate(db);
        }
    }
}
