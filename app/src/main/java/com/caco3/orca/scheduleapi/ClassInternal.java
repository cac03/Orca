package com.caco3.orca.scheduleapi;

import com.google.gson.annotations.SerializedName;

/**
 * They mean lesson.
 *
 * This class is part of {@link ScheduleApiResponseInternal}.
 * It shall not go out of this package.
 * It must be adapted for using in application
 */
/*package*/ class ClassInternal {

    /**
     * Don't know what they mean
     */
    @SerializedName("Code")
    String code;

    /**
     * Full name of lesson
     */
    @SerializedName("Name")
    String name;

    /**
     * Shorten name of teacher
     */
    @SerializedName("Teacher")
    String teacherName;

    /**
     * Full name of teacher
     */
    @SerializedName("TeacherFullName")
    String teacherFullName;

    @Override
    public String toString() {
        return "ClassInternal{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", teacherFullName='" + teacherFullName + '\'' +
                '}';
    }
}
