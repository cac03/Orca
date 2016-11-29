package com.caco3.orca.orioks.model;


import java.util.List;

/**
 * Interface for response returned by {@link com.caco3.orca.orioks.Orioks}
 */
public interface OrioksResponse {

    /**
     * Returns a list of disciplines for user who credentials was used to get this response
     * @return list
     */
    List<Discipline> getDisciplines();

    /**
     * Returns a {@link Student} associated in the university with credentials used to get this respose
     * @return {@link Student}
     */
    Student getStudent();

    /**
     * Returns number of current semester for {@link Student}
     * associated in the university with credentials used to get this respose
     *
     * @return int
     */
    int getCurrentSemester();

    /**
     * Returns current week in the university
     * @return int
     */
    int getCurrentWeek();
}
