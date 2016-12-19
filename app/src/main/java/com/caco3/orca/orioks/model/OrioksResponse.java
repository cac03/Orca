package com.caco3.orca.orioks.model;


import com.caco3.orca.util.Preconditions;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for response returned by {@link com.caco3.orca.orioks.Orioks}
 */
public class OrioksResponse implements Serializable{

    private final List<Discipline> disciplines;

    private final Student student;
    private final int currentSemester;
    private final int currentWeek;
    private static final long serialVersionUID = -87987654643123132L;

    public OrioksResponse(List<Discipline> disciplines, Student student, int currentSemester, int currentWeek) {
        this.disciplines = Preconditions.checkNotNull(disciplines);
        this.student = Preconditions.checkNotNull(student);
        this.currentSemester = currentSemester;
        this.currentWeek = currentWeek;
    }

    /**
     * Returns a list of disciplines for user who credentials was used to get this response
     * @return list
     */
    public List<Discipline> getDisciplines(){
        return disciplines;
    }

    /**
     * Returns a {@link Student} associated in the university with credentials used to get this respose
     * @return {@link Student}
     */
    public Student getStudent(){
        return student;
    }

    /**
     * Returns number of current semester for {@link Student}
     * associated in the university with credentials used to get this respose
     *
     * @return int
     */
    public int getCurrentSemester(){
        return currentSemester;
    }

    /**
     * Returns current week in the university
     * @return int
     */
    public int getCurrentWeek(){
        return currentWeek;
    }
}
