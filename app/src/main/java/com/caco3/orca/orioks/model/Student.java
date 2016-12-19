package com.caco3.orca.orioks.model;

import com.caco3.orca.util.Preconditions;

import java.io.Serializable;

/**
 * Student model class
 */
public class Student implements Serializable {

    /**
     * Associated with this student
     */
    private final Group group;

    // not null
    private final String firstName;
    // not null
    private final String lastName;
    // nullable
    private final String patronymic;

    private static final long serialVersionUID = -87889546253458945L;


    public Student(Group group, String firstName, String lastName, String patronymic) {
        this.group = Preconditions.checkNotNull(group);
        this.firstName = Preconditions.checkNotNull(firstName);
        this.lastName = Preconditions.checkNotNull(lastName);
        this.patronymic = patronymic;
    }

    public Group getGroup() {
        return group;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (group != null ? !group.equals(student.group) : student.group != null) return false;
        if (firstName != null ? !firstName.equals(student.firstName) : student.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(student.lastName) : student.lastName != null)
            return false;
        return patronymic != null ? patronymic.equals(student.patronymic) : student.patronymic == null;

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        return result;
    }
}
