package com.caco3.orca.data.orioks;

import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.Student;

import java.util.Collection;
import java.util.List;

/**
 * An interface for repository which stores components of {@link com.caco3.orca.orioks.model.OrioksResponse}
 */
public interface OrioksRepository {

    /**
     * Saves student object to the repository
     * @param credentials associated with this student
     * @param student to save
     */
    void saveStudent(UserCredentials credentials, Student student);

    /**
     * Returns {@link Student} object associated with provided {@link UserCredentials}
     * @param userCredentials to get {@link Student} associated with
     * @return {@link Student} or null if no student in repository is associated with {@link UserCredentials}
     */
    Student getStudent(UserCredentials userCredentials);

    /**
     * Saves collection of discipline to the repository
     * @param credentials disciplines with are associated
     * @param disciplines to save
     * @param semester when this disciplines appear
     */
    void saveDisciplines(UserCredentials credentials, Collection<Discipline> disciplines, int semester);

    /**
     * Returns list of disciplines associated with provided {@link UserCredentials}
     * @param credentials associated with list
     * @param semester to get disciplines for
     * @return List of disciplines or null, if no disciplines were saved
     */
    List<Discipline> getDisciplines(UserCredentials credentials, int semester);

    /**
     * Associates 'current semester' value with user credentials
     * @param credentials to set for
     * @param semester to set to
     */
    void setCurrentSemester(UserCredentials credentials, int semester);

    /**
     * Returns current semester value associated with provided {@link UserCredentials}
     * which was previously set via {@link #setCurrentSemester(UserCredentials, int)}
     * @param userCredentials to get value for
     * @return current semester value associated with credentials, or -1 if no value was associated
     * previously
     */
    int getCurrentSemester(UserCredentials userCredentials);
}
