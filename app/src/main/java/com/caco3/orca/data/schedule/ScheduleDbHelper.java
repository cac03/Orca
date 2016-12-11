package com.caco3.orca.data.schedule;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.caco3.orca.schedule.model.Lesson;

/*package*/final class ScheduleDbHelper extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "schedule.db";
    private static final int DB_VERSION = 4;

    /*package*/ ScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Groups.onCreate(db);
        Lessons.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Groups.onUpgrade(db, oldVersion, newVersion);
        Lessons.onUpgrade(db, oldVersion, newVersion);
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

        @SuppressWarnings("unused")
        private static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Table for {@link com.caco3.orca.schedule.model.Lesson}.
     */
    /*package*/ static class Lessons {
        /*package*/ static final String TABLE_NAME = "lessons";
        /*package*/ static final String KEY__ID = "_id";
        /** {@link Lesson#getTeacherName()}*/
        /*package*/ static final String KEY_TEACHER = "teacher";
        /** {@link Lesson#getClassroom()} */
        /*package*/ static final String KEY_CLASSROOM = "classroom";
        /** {@link Lesson#getBeginMillis()} */
        /*package*/ static final String KEY_BEGIN_AT = "begin";
        /** {@link Lesson#getEndMillis()} */
        /*package*/ static final String KEY_END_AT = "end";
        /** {@link Lesson#getDisciplineName()} */
        /*package*/ static final String KEY_DISCIPLINE = "discipline";
        /**
         * Mapped from
         * {@link Lesson#isLaboratoryWork()}
         * {@link Lesson#isSeminar()}
         * {@link Lesson#isLecture()}
         * {@link Lesson#isPhysicalEducation()}
         * {@link Lesson#isMilitaryLesson()}
         * by {@link ScheduleRepositoryDbImpl}
         * @see ScheduleRepositoryDbImpl#TYPE_LABORATORY_WORK
         * @see ScheduleRepositoryDbImpl#TYPE_LECTURE
         * @see ScheduleRepositoryDbImpl#TYPE_MILITARY_LESSON
         * @see ScheduleRepositoryDbImpl#TYPE_SEMINAR
         * @see ScheduleRepositoryDbImpl#TYPE_PHYSICAL_EDUCATION
         */
        /*package*/ static final String KEY_TYPE = "type";
        /** Associated with lesson */
        /*package*/ static final String KEY_GROUP_NAME = "group_name";

        private static void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY__ID + " INTEGER PRIMARY KEY, "
                    + KEY_TEACHER + " TEXT, "
                    + KEY_CLASSROOM + " TEXT, "
                    + KEY_DISCIPLINE + " TEXT, "
                    + KEY_BEGIN_AT + " INTEGER, "
                    + KEY_END_AT + " INTEGER, "
                    + KEY_TYPE + " INTEGER, "
                    + KEY_GROUP_NAME + " TEXT"
                    + ")");
        }

        @SuppressWarnings("unused")
        private static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
