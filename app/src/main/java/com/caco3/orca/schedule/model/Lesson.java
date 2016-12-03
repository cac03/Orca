package com.caco3.orca.schedule.model;

/**
 * A lesson model
 * Used in views to show schedule. It's a part of {@link DaySchedule}
 * @see com.caco3.orca.scheduleapi.ScheduleItem
 */
public class Lesson {

    private String disciplineName;

    private long beginMillis;

    private long endMillis;

    private String classroom;

    private String teacherName;

    private int orderInDay;

    private boolean isLecture;

    private boolean isSeminar;

    private boolean isLaboratoryWork;

    private boolean isPhysicalEducation;

    private boolean isMilitaryLesson;

    private Lesson(){

    }
    public Lesson(String disciplineName, long beginMillis, long endMillis, String classroom, String teacherName, int orderInDay, boolean isLecture, boolean isSeminar, boolean isLaboratoryWork, boolean isPhysicalEducation, boolean isMilitaryLesson) {
        this.disciplineName = disciplineName;
        this.beginMillis = beginMillis;
        this.endMillis = endMillis;
        this.classroom = classroom;
        this.teacherName = teacherName;
        this.orderInDay = orderInDay;
        this.isLecture = isLecture;
        this.isSeminar = isSeminar;
        this.isLaboratoryWork = isLaboratoryWork;
        this.isPhysicalEducation = isPhysicalEducation;
        this.isMilitaryLesson = isMilitaryLesson;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public long getBeginMillis() {
        return beginMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getOrderInDay() {
        return orderInDay;
    }

    public boolean isLecture() {
        return isLecture;
    }

    public boolean isSeminar() {
        return isSeminar;
    }

    public boolean isLaboratoryWork() {
        return isLaboratoryWork;
    }

    public boolean isPhysicalEducation() {
        return isPhysicalEducation;
    }

    public boolean isMilitaryLesson() {
        return isMilitaryLesson;
    }

    private boolean isNoLesson;

    public boolean isNoLesson(){
        return isNoLesson;
    }

    /**
     * Means no lesson
     * @param orderInDay when it takes place in day
     * @param beginMillis begin of lesson
     * @param endMillis end of lesson
     * @return 'noLesson' lesson, all fields, except {@link #orderInDay},
     * {@link #beginMillis}, {@link #endMillis}, {@link #isNoLesson}
     * are not set. Use {@link #isNoLesson()} to determine whether this lesson is noLesson
     */
    public static Lesson noLesson(int orderInDay, long beginMillis, long endMillis) {
        Lesson lesson = new Lesson();
        lesson.orderInDay = orderInDay;
        lesson.beginMillis = beginMillis;
        lesson.endMillis = endMillis;
        lesson.isNoLesson = true;

        return lesson;
    }
}
