package com.caco3.orca.data.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.caco3.orca.scheduleapi.ScheduleItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    public void saveSchedule(String groupName, Set<ScheduleItem> scheduleItems) {
        Timber.i("Going to save schedule for group: %s", groupName);
        SQLiteDatabase db = null;
        try {
            db = new ScheduleDbHelper(context).getWritableDatabase();
            db.beginTransaction();

            Timber.d("Transaction begun");
            // remove old entries
            db.delete(ScheduleDbHelper.ScheduleItems.TABLE_NAME,
                    ScheduleDbHelper.ScheduleItems.KEY_GROUP_NAME + " = ?",
                    new String[]{groupName});
            Timber.d("Old entries removed");
            // insert new
            for (ScheduleItem item : scheduleItems) {
                db.insert(ScheduleDbHelper.ScheduleItems.TABLE_NAME,
                        null,
                        contentValuesFromScheduleItem(groupName, item));
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
    public List<ScheduleItem> getSchedule(String groupName) {
        Timber.i("Going to get schedule for group: %s", groupName);
        SQLiteDatabase db = null;
        Cursor cursor = null;

        List<ScheduleItem> result = new ArrayList<>(30);

        try {
            db = new ScheduleDbHelper(context).getReadableDatabase();

            String selection = ScheduleDbHelper.ScheduleItems.KEY_GROUP_NAME + " = ?";
            String[] args = new String[]{groupName};
            String orderBy = ScheduleDbHelper.ScheduleItems.KEY_DAY_OF_WEEK
                    + ", " + ScheduleDbHelper.ScheduleItems.KEY_ORDER_IN_DAY;

            Timber.i("Querying schedule for group: %s", groupName);
            cursor = db.query(ScheduleDbHelper.ScheduleItems.TABLE_NAME,
                    null, // columns
                    selection,
                    args,
                    null, // groupBy
                    null, // having
                    orderBy);

            if (cursor != null && cursor.getCount() > 0) {
                Timber.i("Query successful. Available items: %d", cursor.getCount());
                while (cursor.moveToNext()) {
                    result.add(scheduleItemFromCursor(cursor));
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

    @Override
    public void removeSchedule(String groupName) {
        Timber.i("Going to remove schedule for %s from db", groupName);
        SQLiteDatabase db = null;
        try {
            db = new ScheduleDbHelper(context).getWritableDatabase();
            db.delete(ScheduleDbHelper.ScheduleItems.TABLE_NAME,
                    ScheduleDbHelper.ScheduleItems.KEY_GROUP_NAME + " = ?",
                    new String[]{groupName});
            Timber.i("Successfully removed schedule for %s from db", groupName);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Static method creates {@link ContentValues} from {@link ScheduleItem}
     * which is ready to be used in {@link SQLiteDatabase#insert(String, String, ContentValues)}
     * into {@link com.caco3.orca.data.schedule.ScheduleDbHelper.ScheduleItems} table
     *
     * @param groupName associated with scheduleItem
     * @param scheduleItem to extract values from
     * @return {@link ContentValues} with values extracted from provided scheduleItem
     */
    private static ContentValues contentValuesFromScheduleItem(String groupName, ScheduleItem scheduleItem) {
        ContentValues cv = new ContentValues(20);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_DAY_OF_WEEK, scheduleItem.getDayOfWeek());
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_ORDER_IN_DAY, scheduleItem.getOrderInDay());
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_FIRST_WEEK_OF_MONTH,
                scheduleItem.repeatsEveryFirstWeekOfMonth() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_SECOND_WEEK_OF_MONTH,
                scheduleItem.repeatsEverySecondWeekOfMonth() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_THIRD_WEEK_OF_MONTH,
                scheduleItem.repeatsEveryThirdWeekOfMonth() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_FOURTH_WEEK_OF_MONTH,
                scheduleItem.repeatsEveryFourthWeekOfMonth() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_DISCIPLINE_NAME, scheduleItem.getDisciplineName());
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_IS_LECTURE, scheduleItem.isLecture() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_IS_SEMINAR, scheduleItem.isSeminar() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_IS_LABORATORY_WORK,
                scheduleItem.isLaboratoryWork() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_CLASSROOM, scheduleItem.getClassroom());
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_TEACHER_NAME, scheduleItem.getTeacherName());
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_IS_PHYSICAL_EDUCATION,
                scheduleItem.isPhysicalEducation() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_IS_MILITARY_LESSON,
                scheduleItem.isMilitaryLesson() ? 1 : 0);
        cv.put(ScheduleDbHelper.ScheduleItems.KEY_GROUP_NAME, groupName);

        return cv;
    }

    /**
     * Static method creates {@link ScheduleItem} from provided cursor
     * @param c to create from
     * @return {@link ScheduleItem} created from provided cursor
     */
    private static ScheduleItem scheduleItemFromCursor(Cursor c) {
        int dayOfWeekIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_DAY_OF_WEEK);
        int orderInDayIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_ORDER_IN_DAY);
        int repeatsEveryFirstWeekOfMonthIdx
                = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_FIRST_WEEK_OF_MONTH);
        int repeatsEverySecondWeekOfMonthIdx
                = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_SECOND_WEEK_OF_MONTH);
        int repeatsEveryThirdWeekOfMonthIdx
                = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_THIRD_WEEK_OF_MONTH);
        int repeatsEveryFourthWeekOfMonth
                = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_REPEATS_EVERY_FOURTH_WEEK_OF_MONTH);
        int disciplineNameIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_DISCIPLINE_NAME);
        int isLectureIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_IS_LECTURE);
        int isSeminarIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_IS_SEMINAR);
        int isLaboratoryWork = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_IS_LABORATORY_WORK);
        int classroomIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_CLASSROOM);
        int teacherNameIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_TEACHER_NAME);
        int isPhysicalEducationIdx= c.getColumnIndex(ScheduleDbHelper.ScheduleItems.KEY_IS_PHYSICAL_EDUCATION);
        int isMilitaryLessonIdx = c.getColumnIndexOrThrow(ScheduleDbHelper.ScheduleItems.KEY_IS_MILITARY_LESSON);

        return ScheduleItem
                .builder()
                .dayOfWeek(c.getInt(dayOfWeekIdx))
                .setOrderInDay(c.getInt(orderInDayIdx))
                .repeatsEveryFirstWeekOfMonth(c.getInt(repeatsEveryFirstWeekOfMonthIdx) == 1)
                .repeatsEverySecondWeekOfMonth(c.getInt(repeatsEverySecondWeekOfMonthIdx) == 1)
                .repeatsEveryThirdWeekOfMonth(c.getInt(repeatsEveryThirdWeekOfMonthIdx) == 1)
                .repeatsEveryFourthWeekOfMonth(c.getInt(repeatsEveryFourthWeekOfMonth) == 1)
                .disciplineName(c.getString(disciplineNameIdx))
                .lecture(c.getInt(isLectureIdx) == 1)
                .seminar(c.getInt(isSeminarIdx) == 1)
                .laboratoryWork(c.getInt(isLaboratoryWork) == 1)
                .classroom(c.getString(classroomIdx))
                .teacherName(c.getString(teacherNameIdx))
                .physicalEducation(c.getInt(isPhysicalEducationIdx) == 1)
                .militaryLesson(c.getInt(isMilitaryLessonIdx) == 1)
                .build();
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
}
