package com.caco3.orca.data.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.caco3.orca.schedule.model.Lesson;
import com.caco3.orca.scheduleapi.ScheduleItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Concrete implementation of {@link ScheduleRepository} where data is stored using {@link android.database.sqlite.SQLiteDatabase}
 */
/*package*/ class ScheduleRepositoryDbImpl implements ScheduleRepository {

    private final Context context;

    @Inject
    /*package*/ ScheduleRepositoryDbImpl(Context context) {
        this.context = context;
    }

    @Override
    public void saveGroupNames(Collection<String> collection) {
        SQLiteDatabase db = null;

        Timber.i("Going to save collection of group names to db");
        try {
            db = new ScheduleDbHelper(context).getWritableDatabase();

            db.beginTransaction();
            Timber.i("Transaction began");

            for(String groupName : collection) {
                ContentValues cv = new ContentValues(1);
                cv.put(ScheduleDbHelper.Groups.KEY_GROUP_NAME, groupName);
                db.insert(ScheduleDbHelper.Groups.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();
            Timber.i("Transaction successful");
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public List<String> getGroupNames() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Timber.i("Going to get group names from db");
        try {
            db = new ScheduleDbHelper(context).getReadableDatabase();

            String[] columns = new String[]{ScheduleDbHelper.Groups.KEY_GROUP_NAME};
            String orderBy = ScheduleDbHelper.Groups.KEY_GROUP_NAME;
            cursor = db.query(ScheduleDbHelper.Groups.TABLE_NAME,
                    columns,
                    null, // selection
                    null, // selection args
                    null, // group by
                    null, // having
                    orderBy);

            Timber.i("Queried");

            if (cursor != null) {
                Timber.i("Available: %d", cursor.getCount());
                if (cursor.getCount() == 0) {
                    /**
                     * No items saved. We have to return null according to {@link ScheduleRepository}
                     * contract
                     */
                    return null;
                } else {
                    int nameIdx = cursor.getColumnIndexOrThrow(ScheduleDbHelper.Groups.KEY_GROUP_NAME);
                    List<String> result = new ArrayList<>(250/* approximately number of groups */ );
                    while (cursor.moveToNext()) {
                        result.add(cursor.getString(nameIdx));
                    }

                    return result;
                }
            } else {
                return null;
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void saveSchedule(List<Lesson> lessons, String groupName) {
        Timber.i("Going to save schedule for group: %s", groupName);
        SQLiteDatabase db = null;
        try {
            db = new ScheduleDbHelper(context).getWritableDatabase();
            db.beginTransaction();

            Timber.d("Transaction begun");
            // remove old entries
            db.delete(ScheduleDbHelper.Lessons.TABLE_NAME,
                    ScheduleDbHelper.Lessons.KEY_GROUP_NAME + " = ?",
                    new String[]{groupName});
            Timber.d("Old entries removed");
            // insert new
            for (Lesson item : lessons) {
                db.insert(ScheduleDbHelper.Lessons.TABLE_NAME,
                        null,
                        contentValuesFromLesson(item, groupName));
            }
            Timber.d("New entries inserted");

            db.setTransactionSuccessful();
            Timber.i("Successfully saved schedule");
        } finally {
            if (db != null) {
                db.endTransaction();
                Timber.d("Transaction ended");
                db.close();
            }
        }
    }

    @Override
    public List<Lesson> getSchedule(String groupName, long from) {
        return getSchedule(groupName, from, Long.MAX_VALUE);
    }

    @Override
    public List<Lesson> getSchedule(String groupName) {
        return getSchedule(groupName, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public List<Lesson> getSchedule(String groupName, long from, long to) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Lesson> result = new ArrayList<>();
        try {
            db = new ScheduleDbHelper(context).getReadableDatabase();
            String selection =
                    ScheduleDbHelper.Lessons.KEY_GROUP_NAME + " = ? "
                    + " AND " + ScheduleDbHelper.Lessons.KEY_BEGIN_AT + " >= ? "
                    + " AND " + ScheduleDbHelper.Lessons.KEY_END_AT + " <= ? ";
            String[] args = new String[]{groupName, from + "", to + ""};
            String orderBy = ScheduleDbHelper.Lessons.KEY_BEGIN_AT;
            cursor = db.query(ScheduleDbHelper.Lessons.TABLE_NAME,
                    null, // columns
                    selection,
                    args,
                    null, // group by
                    null, // having
                    orderBy);

            if (cursor != null &&  cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    result.add(lessonFromCursor(cursor));
                }
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        }

        return result;
    }

    /**
     * Static method creates {@link Lesson} instance from {@link Cursor}
     * which was queried from {@link com.caco3.orca.data.schedule.ScheduleDbHelper.Lessons} table
     * @param c to create from
     * @return {@link Lesson}
     */
    private static Lesson lessonFromCursor(Cursor c) {
        int disciplineNameIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_DISCIPLINE);
        int classroomIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_CLASSROOM);
        int teacherNameIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_TEACHER);
        int beginTimeIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_BEGIN_AT);
        int endTimeIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_END_AT);
        int typeIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.Lessons.KEY_TYPE);


        int type = c.getInt(typeIdx);

        return Lesson
                .builder()
                .beginAt(c.getLong(beginTimeIdx))
                .endAt(c.getLong(endTimeIdx))
                .discipline(c.getString(disciplineNameIdx))
                .classroom(c.getString(classroomIdx))
                .teacher(c.getString(teacherNameIdx))
                .lecture(type == TYPE_LECTURE)
                .seminar(type == TYPE_SEMINAR)
                .laboratoryWork(type == TYPE_LABORATORY_WORK)
                .physicalEducation(type == TYPE_PHYSICAL_EDUCATION)
                .militaryLesson(type == TYPE_MILITARY_LESSON)
                .build();

    }

    /**
     * Creates {@link ContentValues} ready to be inserted into {@link com.caco3.orca.data.schedule.ScheduleDbHelper.Lessons}
     * table
     * @param lesson to create from
     * @param groupName associated with lesson
     * @return {@link ContentValues}
     */
    private static ContentValues contentValuesFromLesson(Lesson lesson, String groupName) {
        ContentValues cv = new ContentValues();

        cv.put(ScheduleDbHelper.Lessons.KEY_DISCIPLINE, lesson.getDisciplineName());
        int type;
        if (lesson.isLecture()) {
            type = TYPE_LECTURE;
        } else if (lesson.isSeminar()) {
            type = TYPE_SEMINAR;
        } else if (lesson.isLaboratoryWork()) {
            type = TYPE_LABORATORY_WORK;
        } else if (lesson.isPhysicalEducation()) {
            type = TYPE_PHYSICAL_EDUCATION;
        } else {
            type = TYPE_MILITARY_LESSON;
        }
        cv.put(ScheduleDbHelper.Lessons.KEY_TYPE, type);
        cv.put(ScheduleDbHelper.Lessons.KEY_CLASSROOM, lesson.getClassroom());
        cv.put(ScheduleDbHelper.Lessons.KEY_TEACHER, lesson.getTeacherName());
        cv.put(ScheduleDbHelper.Lessons.KEY_GROUP_NAME, groupName);
        cv.put(ScheduleDbHelper.Lessons.KEY_BEGIN_AT, lesson.getBeginMillis());
        cv.put(ScheduleDbHelper.Lessons.KEY_END_AT, lesson.getEndMillis());

        return cv;
    }

    private static final int TYPE_LECTURE = 0;
    private static final int TYPE_SEMINAR = 1;
    private static final int TYPE_LABORATORY_WORK = 2;
    private static final int TYPE_PHYSICAL_EDUCATION = 3;
    private static final int TYPE_MILITARY_LESSON = 4;
}
