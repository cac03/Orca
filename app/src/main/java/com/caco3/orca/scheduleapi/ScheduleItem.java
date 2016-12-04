package com.caco3.orca.scheduleapi;

import com.caco3.orca.miet.MietUtils;
import com.caco3.orca.util.Preconditions;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Final, immutable model for schedule item.
 * It's not merely one schedule item in one day, it's more general
 * use {@link #takesPlaceAt(Calendar)} to know whether this schedule item might be
 * insert in day schedule.
 */
public final class ScheduleItem implements Serializable {
    /**
     * Corresponding to java's {@link Calendar#DAY_OF_WEEK}
     */
    private final int dayOfWeek;

    /**
     * Order of this lesson in a day
     */
    private final int orderInDay;

    /**
     * True if this repeats every first week of month
     */
    private final boolean repeatsEveryFirstWeekOfMonth;

    /**
     * True if this repeats every second week of month
     */
    private final boolean repeatsEverySecondWeekOfMonth;

    /**
     * True if this repeats every third week of month
     */
    private final boolean repeatsEveryThirdWeekOfMonth;

    /**
     * True if this repeats every fourth week of month
     */
    private final boolean repeatsEveryFourthWeekOfMonth;

    /**
     * Name of discipline associated with this schedule item
     */
    private final String disciplineName;

    /**
     * True if this is lecture
     */
    private final boolean isLecture;

    /**
     * True if seminar
     */
    private final boolean isSeminar;

    /**
     * True if laboratory work
     */
    private final boolean isLaboratoryWork;

    /**
     * String representation of classroom where this schedule item takes a place
     */
    private final String classroom;

    /**
     * Shorten teacher name for this schedule item
     */
    private final String teacherName;

    /**
     * True if this item associated with physical education
     */
    private final boolean isPhysicalEducation;

    /**
     * True if this item associated with military lesson
     */
    private final boolean isMilitaryLesson;

    /**for {@link Serializable}*/
    private static final long serialVersionUID = -100855578946L;

    /**
     * Private c-tor to prevent facing clients with this many-argument c-tor.
     * Clients shall use {@link Builder} to create instance
     *
     * @param builder to construct item from
     * @throws NullPointerException if any non-primitive field is <code>null</code>
     */
    private ScheduleItem(Builder builder) {
        this.dayOfWeek = builder.dayOfWeek;
        this.orderInDay = builder.orderInDay;
        this.repeatsEveryFirstWeekOfMonth = builder.repeatsEveryFirstWeekOfMonth;
        this.repeatsEverySecondWeekOfMonth = builder.repeatsEverySecondWeekOfMonth;
        this.repeatsEveryThirdWeekOfMonth = builder.repeatsEveryThirdWeekOfMonth;
        this.repeatsEveryFourthWeekOfMonth = builder.repeatsEveryFourthWeekOfMonth;
        this.disciplineName = Preconditions.checkNotNull(builder.disciplineName);
        this.isLecture = builder.isLecture;
        this.isSeminar = builder.isSeminar;
        this.isLaboratoryWork = builder.isLaboratoryWork;
        this.classroom = Preconditions.checkNotNull(builder.classroom);
        this.teacherName = Preconditions.checkNotNull(builder.teacherName);
        this.isPhysicalEducation = builder.physicalEducation;
        this.isMilitaryLesson = builder.militaryLesson;
    }

    /**
     * Returns true if this schedule item takes place at provided calendar time.
     * It takes into account only {@link Calendar#DAY_OF_YEAR} and doesn't depend on
     * anything that more precisely than {@link Calendar#DAY_OF_YEAR}
     *
     * @param calendar to test for
     * @return true if this schedule item takes place at provided calendar time, false otherwise
     */
    public boolean takesPlaceAt(Calendar calendar) {
        boolean autumnSemester = MietUtils.belongsToAutumnSemester(calendar);
        boolean springSemester = MietUtils.belongsToSpringSemester(calendar);
        if (!autumnSemester && !springSemester) {
            // recess. No lessons
            return false;
        }

        if (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            return false;
        }

        int weekOfSemester = MietUtils.getWeekOfSemester(calendar) - 1;

        /*
         * What week of month is provided
         */
        switch (weekOfSemester % 4) {
            case 0:
                return repeatsEveryFirstWeekOfMonth;
            case 1:
                return repeatsEverySecondWeekOfMonth;
            case 2:
                return repeatsEveryThirdWeekOfMonth;
            case 3:
                return repeatsEveryFourthWeekOfMonth;
            default:
                // this will never happen
                throw new AssertionError();
        }
    }

    public int getOrderInDay() {
        return orderInDay;
    }

    public boolean repeatsEveryFirstWeekOfMonth() {
        return repeatsEveryFirstWeekOfMonth;
    }

    public boolean repeatsEverySecondWeekOfMonth() {
        return repeatsEverySecondWeekOfMonth;
    }

    public boolean repeatsEveryThirdWeekOfMonth() {
        return repeatsEveryThirdWeekOfMonth;
    }

    public boolean repeatsEveryFourthWeekOfMonth() {
        return repeatsEveryFourthWeekOfMonth;
    }

    public String getDisciplineName() {
        return disciplineName;
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

    public String getClassroom() {
        return classroom;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isPhysicalEducation() {
        return isPhysicalEducation;
    }

    public boolean isMilitaryLesson() {
        return isMilitaryLesson;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private int dayOfWeek;
        private int orderInDay;
        private boolean repeatsEveryFirstWeekOfMonth;
        private boolean repeatsEverySecondWeekOfMonth;
        private boolean repeatsEveryThirdWeekOfMonth;
        private boolean repeatsEveryFourthWeekOfMonth;
        private String disciplineName;
        private boolean isLecture;
        private boolean isSeminar;
        private boolean isLaboratoryWork;
        private String classroom;
        private String teacherName;
        private boolean physicalEducation;
        private boolean militaryLesson;

        public Builder dayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public Builder setOrderInDay(int orderInDay) {
            this.orderInDay = orderInDay;
            return this;
        }

        public Builder repeatsEveryFirstWeekOfMonth(boolean repeatsEveryFirstWeekOfMonth) {
            this.repeatsEveryFirstWeekOfMonth = repeatsEveryFirstWeekOfMonth;
            return this;
        }

        public Builder repeatsEverySecondWeekOfMonth(boolean repeatsEverySecondWeekOfMonth) {
            this.repeatsEverySecondWeekOfMonth = repeatsEverySecondWeekOfMonth;
            return this;
        }

        public Builder repeatsEveryThirdWeekOfMonth(boolean repeatsEveryThirdWeekOfMonth) {
            this.repeatsEveryThirdWeekOfMonth = repeatsEveryThirdWeekOfMonth;
            return this;
        }

        public Builder repeatsEveryFourthWeekOfMonth(boolean repeatsEveryFourthWeekOfMonth) {
            this.repeatsEveryFourthWeekOfMonth = repeatsEveryFourthWeekOfMonth;
            return this;
        }

        public Builder disciplineName(String disciplineName) {
            this.disciplineName = disciplineName;
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

        public Builder classroom(String classroom) {
            this.classroom = classroom;
            return this;
        }

        public Builder teacherName(String teacherName) {
            this.teacherName = teacherName;
            return this;
        }

        public Builder physicalEducation(boolean physicalEducation) {
            this.physicalEducation = physicalEducation;
            return this;
        }

        public Builder militaryLesson(boolean militaryLesson) {
            this.militaryLesson = militaryLesson;
            return this;
        }

        public ScheduleItem build(){
            return new ScheduleItem(this);
        }

        /**
         * Internal method used in {@link com.caco3.orca.scheduleapi.ResponseAdapter}.
         *
         * Compares two builders and returns true if all their fields are equal
         * except following:
         * {@link #repeatsEveryFirstWeekOfMonth},
         * {@link #repeatsEverySecondWeekOfMonth},
         * {@link #repeatsEveryThirdWeekOfMonth},
         * {@link #repeatsEveryFourthWeekOfMonth}.
         *
         * Otherwise returns false.
         *
         * @param that builder to compare with
         * @return true if two builders equal except 'repeats' fields
         */
        /*package*/ boolean areSameExceptRepeats(Builder that) {

            if (dayOfWeek != that.dayOfWeek) return false;
            if (orderInDay != that.orderInDay) return false;
            if (isLecture != that.isLecture) return false;
            if (isSeminar != that.isSeminar) return false;
            if (isLaboratoryWork != that.isLaboratoryWork) return false;
            if (disciplineName != null ? !disciplineName.equals(that.disciplineName) : that.disciplineName != null)
                return false;
            if (classroom != null ? !classroom.equals(that.classroom) : that.classroom != null)
                return false;
            if (militaryLesson != that.militaryLesson)
                return false;
            if (physicalEducation != that.physicalEducation)
                return false;
            return teacherName != null ? teacherName.equals(that.teacherName) : that.teacherName == null;

        }

        /**
         * Internal method user in {@link com.caco3.orca.scheduleapi.ResponseAdapter}
         * Reads 'repeats' field and if some of them are true in other builder,
         * mark them true in this builder
         *
         * @param other builder to merger with
         */
        /*package*/ void mergeByRepeats(Builder other) {
            if (other.repeatsEveryFirstWeekOfMonth) {
                this.repeatsEveryFirstWeekOfMonth = true;
            }

            if (other.repeatsEverySecondWeekOfMonth) {
                this.repeatsEverySecondWeekOfMonth = true;
            }

            if (other.repeatsEveryThirdWeekOfMonth) {
                this.repeatsEveryThirdWeekOfMonth = true;
            }

            if (other.repeatsEveryFourthWeekOfMonth) {
                this.repeatsEveryFourthWeekOfMonth = true;
            }
        }

        /**
         * Internal method used in {@link ResponseAdapter}.
         * It compares to builders by all fields except {@link #classroom}
         * @param that builder to compare with
         * @return true if all fields are same except {@link #classroom}
         */
        /*package*/ boolean areSameExceptClassroom(Builder that) {
            if (dayOfWeek != that.dayOfWeek) return false;
            if (orderInDay != that.orderInDay) return false;
            if (repeatsEveryFirstWeekOfMonth != that.repeatsEveryFirstWeekOfMonth) return false;
            if (repeatsEverySecondWeekOfMonth != that.repeatsEverySecondWeekOfMonth)
                return false;
            if (repeatsEveryThirdWeekOfMonth != that.repeatsEveryThirdWeekOfMonth) return false;
            if (repeatsEveryFourthWeekOfMonth != that.repeatsEveryFourthWeekOfMonth)
                return false;
            if (isLecture != that.isLecture) return false;
            if (isSeminar != that.isSeminar) return false;
            if (isLaboratoryWork != that.isLaboratoryWork) return false;
            if (physicalEducation != that.physicalEducation) return false;
            if (militaryLesson != that.militaryLesson) return false;
            if (disciplineName != null ? !disciplineName.equals(that.disciplineName) : that.disciplineName != null)
                return false;
            return teacherName != null ? teacherName.equals(that.teacherName) : that.teacherName == null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Builder builder = (Builder) o;

            if (dayOfWeek != builder.dayOfWeek) return false;
            if (orderInDay != builder.orderInDay) return false;
            if (repeatsEveryFirstWeekOfMonth != builder.repeatsEveryFirstWeekOfMonth) return false;
            if (repeatsEverySecondWeekOfMonth != builder.repeatsEverySecondWeekOfMonth)
                return false;
            if (repeatsEveryThirdWeekOfMonth != builder.repeatsEveryThirdWeekOfMonth) return false;
            if (repeatsEveryFourthWeekOfMonth != builder.repeatsEveryFourthWeekOfMonth)
                return false;
            if (isLecture != builder.isLecture) return false;
            if (isSeminar != builder.isSeminar) return false;
            if (isLaboratoryWork != builder.isLaboratoryWork) return false;
            if (physicalEducation != builder.physicalEducation) return false;
            if (militaryLesson != builder.militaryLesson) return false;
            if (disciplineName != null ? !disciplineName.equals(builder.disciplineName) : builder.disciplineName != null)
                return false;
            if (classroom != null ? !classroom.equals(builder.classroom) : builder.classroom != null)
                return false;
            return teacherName != null ? teacherName.equals(builder.teacherName) : builder.teacherName == null;

        }

        @Override
        public int hashCode() {
            int result = dayOfWeek;
            result = 31 * result + orderInDay;
            result = 31 * result + (repeatsEveryFirstWeekOfMonth ? 1 : 0);
            result = 31 * result + (repeatsEverySecondWeekOfMonth ? 1 : 0);
            result = 31 * result + (repeatsEveryThirdWeekOfMonth ? 1 : 0);
            result = 31 * result + (repeatsEveryFourthWeekOfMonth ? 1 : 0);
            result = 31 * result + (disciplineName != null ? disciplineName.hashCode() : 0);
            result = 31 * result + (isLecture ? 1 : 0);
            result = 31 * result + (isSeminar ? 1 : 0);
            result = 31 * result + (isLaboratoryWork ? 1 : 0);
            result = 31 * result + (classroom != null ? classroom.hashCode() : 0);
            result = 31 * result + (teacherName != null ? teacherName.hashCode() : 0);
            result = 31 * result + (physicalEducation ? 1 : 0);
            result = 31 * result + (militaryLesson ? 1 : 0);
            return result;
        }
    }
}
