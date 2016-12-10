package com.caco3.orca.schedule.model;

import com.caco3.orca.util.Preconditions;

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

    private Lesson(Builder builder) {
        this.disciplineName = builder.disciplineName;
        this.beginMillis = builder.beginMillis;
        this.endMillis = builder.endMillis;
        this.classroom = builder.classroom;
        this.teacherName = builder.teacherName;
        this.orderInDay = builder.orderInDay;
        this.isLecture = builder.isLecture;
        this.isSeminar = builder.isSeminar;
        this.isLaboratoryWork = builder.isLaboratoryWork;
        this.isPhysicalEducation = builder.isPhysicalEducation;
        this.isMilitaryLesson = builder.isMilitaryLesson;
    }

    public static Builder builder(){
        return new Builder();
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

    public static class Builder {
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

        public Builder discipline(String disciplineName) {
            this.disciplineName = Preconditions.checkNotNull(disciplineName, "disciplineName == null");
            return this;
        }

        public Builder beginAt(long millis) {
            if (millis < 0) {
                throw new IllegalArgumentException("millis < 0. Millis = " + millis);
            }
            this.beginMillis = millis;
            return this;
        }

        public Builder endAt(long millis) {
            if (millis < 0) {
                throw new IllegalArgumentException("millis < 0. Millis = " + millis);
            }
            this.endMillis = millis;
            return this;
        }

        public Builder classroom(String classroom) {
            this.classroom = Preconditions.checkNotNull(classroom, "classroom == null");
            return this;
        }

        public Builder teacher(String teacher) {
            this.teacherName = Preconditions.checkNotNull(teacher, "teacher == null");
            return this;
        }

        public Builder orderInDay(int orderInDay) {
            if (orderInDay < 0) {
                throw new IllegalArgumentException("orderInDay < 0, orderInDay = " + orderInDay);
            }
            this.orderInDay = orderInDay;
            return this;
        }

        public Builder lecture(boolean isLecture) {
            this.isLecture = isLecture;
            return this;
        }

        public Builder seminar(boolean isSeminar) {
            this.isSeminar = isSeminar;
            return this;
        }

        public Builder laboratoryWork(boolean isLaboratoryWork) {
            this.isLaboratoryWork = isLaboratoryWork;
            return this;
        }

        public Builder physicalEducation(boolean isPhysicalEducation) {
            this.isPhysicalEducation = isPhysicalEducation;
            return this;
        }

        public Builder militaryLesson(boolean isMilitaryLesson) {
            this.isMilitaryLesson = isMilitaryLesson;
            return this;
        }

        public Lesson build(){
            ensureRequiredFieldsSet();
            return new Lesson(this);
        }

        private void ensureRequiredFieldsSet(){
            if (disciplineName == null) {
                throw new IllegalStateException("disciplineName must be set");
            }

            if (classroom == null) {
                throw new IllegalStateException("classroom must be set");
            }

            if (teacherName == null) {
                throw new IllegalStateException("teacherName must be set");
            }
        }
    }
}
