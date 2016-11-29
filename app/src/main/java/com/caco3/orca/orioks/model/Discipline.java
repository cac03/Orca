package com.caco3.orca.orioks.model;


import com.caco3.orca.util.Preconditions;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A model class for discipline in the university
 */
public final class Discipline implements Serializable {
    /**
     * Control events associated with this discipline
     */
    private final List<ControlEvent> attachedControlEvents;

    /**
     * Teachers associated with this discipline
     */
    private final List<Teacher> attachedTeachers;

    /**
     * String representation of assessment type for this discipline
     */
    private final String assessmentType;

    /**
     * String representation of name of this discipline
     */
    private final String name;

    /**
     * String representation of department associated with this discipline
     */
    private final String department;

    private static final long serialVersionUID = -8995455245778565588L;

    private Discipline(String department,
                      List<ControlEvent> attachedControlEvents,
                      List<Teacher> attachedTeachers,
                      String assessmentType,
                      String name) {
        this.department = department;
        this.attachedControlEvents = Collections.unmodifiableList(attachedControlEvents);
        this.attachedTeachers = Collections.unmodifiableList(attachedTeachers);
        this.assessmentType = assessmentType;
        this.name = name;
    }

    public Discipline(Builder builder) {
        this(builder.department,
                builder.attachedControlEvents,
                builder.attachedTeachers,
                builder.assessmentType,
                builder.name);
    }

    /**
     * False if any of {@link #attachedControlEvents} {@link ControlEvent#hasSeenNewEnteredPoints}
     * if false.
     * @return false if there if there is any control event where new entered points user has not seen
     * @see {@link ControlEvent#hasSeenNewEnteredPoints}
     */
    public boolean hasSeenAllnewEnteredPoints(){
        for(ControlEvent controlEvent : attachedControlEvents) {
            if (!controlEvent.hasSeenNewEnteredPoints()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sums all achieved points in this discipline
     * @return total sum of all achieved points
     */
    public float getTotalAchievedPoints(){
        float sum = 0F;
        for (ControlEvent controlEvent : attachedControlEvents) {
            sum += controlEvent.getAchievedPoints();
        }

        return sum;
    }

    /**
     * Sums all available points for this discipline at this moment
     * @return sum of all available points for this discipline
     */
    public float getTotalAvailablePoints() {
        float sum = 0F;
        for (ControlEvent controlEvent : attachedControlEvents) {
            if (controlEvent.isEntered()) {
                sum += controlEvent.getMaxAvailablePoints();
            }
        }

        return sum;
    }

    public List<ControlEvent> getAttachedControlEvents() {
        return attachedControlEvents;
    }

    public List<Teacher> getAttachedTeachers() {
        return attachedTeachers;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public static class Builder {
        private List<ControlEvent> attachedControlEvents;
        private List<Teacher> attachedTeachers;
        private String assessmentType;
        private String name;
        private String department;

        public Builder controlEvents(List<ControlEvent> controlEvents){
            this.attachedControlEvents
                    = Preconditions.checkNotNull(controlEvents, "controlEvents == null");

            return this;
        }

        public Builder teacher(List<Teacher> teachers) {
            this.attachedTeachers
                    = Preconditions.checkNotNull(teachers, "teachers == null");

            return this;
        }

        public Builder assessmentType(String assessmentType) {
            this.assessmentType
                    = Preconditions.checkNotNull(assessmentType, "assessmentType == null");

            return this;
        }

        public Builder name(String name) {
            this.name
                    = Preconditions.checkNotNull(name, "name == null");

            return this;
        }

        public Builder department(String department) {
            this.department
                    = Preconditions.checkNotNull(department, "department == null");

            return this;
        }

        public Discipline build(){
            return new Discipline(this);
        }
    }
}
