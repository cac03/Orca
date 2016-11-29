package com.caco3.orca.orioks.model;

import java.io.Serializable;

/**
 * A Control Event attached to Discipline.
 *
 * For creation use {@link Builder}
 */
public final class ControlEvent implements Serializable {
    /**
     * Points achieved by the student for this control event
     */
    private final float achievedPoints;

    /**
     * Maximum points available for this control event
     */
    private final float maxAvailablePoints;

    /**
     * Teacher, who entered points for this control event
     */
    private final Teacher enteredBy;

    /**
     * When this control event takes a place
     */
    private final int week;

    /**
     * String representation of type of this control event
     */
    private final String typeName;

    /**
     * Topic of the control event
     * Nullable
     */
    private final String topic;

    /**
     * True if user has seen new entered points for this control event.
     * False, otherwise.
     *
     * Used to add some mark in ui so the user can quickly determine where new points was entered.
     *
     * Default true
     */
    private boolean hasSeenNewEnteredPoints = true;

    private static final long serialVersionUID = -85465588452256L;

    private ControlEvent(float achievedPoints, float maxAvailablePoints, Teacher enteredBy, int week, String typeName, String topic, boolean hasSeenNewEnteredPoints) {
        this.achievedPoints = achievedPoints;
        this.maxAvailablePoints = maxAvailablePoints;
        this.enteredBy = enteredBy;
        this.week = week;
        this.typeName = typeName;
        this.topic = topic;
        this.hasSeenNewEnteredPoints = hasSeenNewEnteredPoints;
    }

    private ControlEvent(Builder builder) {
        this(builder.achievedPoints,
                builder.maxAvailablePoints,
                builder.enteredBy,
                builder.week,
                builder.typeName,
                builder.topic,
                builder.hasSeenNewEnteredPoints);

    }

    public static Builder builder(){
        return new Builder();
    }

    public void setHasSeenNewEnteredPoints(boolean seen) {
        this.hasSeenNewEnteredPoints = seen;
    }

    public float getAchievedPoints() {
        return achievedPoints;
    }

    public float getMaxAvailablePoints() {
        return maxAvailablePoints;
    }

    public Teacher getEnteredBy() {
        return enteredBy;
    }

    public int getWeek() {
        return week;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isHasSeenNewEnteredPoints() {
        return hasSeenNewEnteredPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlEvent that = (ControlEvent) o;

        if (Float.compare(that.achievedPoints, achievedPoints) != 0) return false;
        if (Float.compare(that.maxAvailablePoints, maxAvailablePoints) != 0) return false;
        if (week != that.week) return false;
        if (hasSeenNewEnteredPoints != that.hasSeenNewEnteredPoints) return false;
        if (enteredBy != null ? !enteredBy.equals(that.enteredBy) : that.enteredBy != null)
            return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null)
            return false;
        return topic != null ? topic.equals(that.topic) : that.topic == null;

    }

    @Override
    public int hashCode() {
        int result = (achievedPoints != +0.0f ? Float.floatToIntBits(achievedPoints) : 0);
        result = 31 * result + (maxAvailablePoints != +0.0f ? Float.floatToIntBits(maxAvailablePoints) : 0);
        result = 31 * result + (enteredBy != null ? enteredBy.hashCode() : 0);
        result = 31 * result + week;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        result = 31 * result + (hasSeenNewEnteredPoints ? 1 : 0);
        return result;
    }

    /**
     * Simplifies creation of new {@link ControlEvent} objects
     */
    public static class Builder {

        private float achievedPoints;
        private float maxAvailablePoints;
        private Teacher enteredBy;
        private int week;
        private String typeName;
        private String topic;
        private boolean hasSeenNewEnteredPoints;

        public Builder achievedPoints(float achievedPoints) {
            this.achievedPoints = achievedPoints;
            return this;
        }

        public Builder maxAvailablePoints(float maxAvailablePoints) {
            this.maxAvailablePoints = maxAvailablePoints;
            return this;
        }

        public Builder enteredBy(Teacher teacher) {
            this.enteredBy = teacher;
            return this;
        }

        public Builder at(int week) {
            this.week = week;
            return this;
        }

        public Builder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;

            return this;
        }

        public Builder hasSeen(boolean seen) {
            this.hasSeenNewEnteredPoints = seen;
            return this;
        }

        public ControlEvent build() {
            return new ControlEvent(this);
        }
    }
}
