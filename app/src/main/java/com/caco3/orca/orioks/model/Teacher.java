package com.caco3.orca.orioks.model;


import com.caco3.orca.util.Preconditions;

import java.io.Serializable;

/**
 * Represents a model of teacher.
 * Immutable
 */
public final class Teacher implements Serializable {

    // not null
    private final String firstName;
    // not null
    private final String lastName;
    // nullable
    private final String patronymic;

    private static final long serialVersionUID = -8865589548664552L;

    public Teacher(String firstName, String lastName, String patronymic) {
        this.firstName = Preconditions.checkNotNull(firstName);
        this.lastName = Preconditions.checkNotNull(lastName);
        this.patronymic = patronymic;
    }

    public Teacher(String fullName) {
        Preconditions.checkNotNull(fullName);
        String[] components = fullName.split(" ");
        this.lastName = components[0];
        this.firstName = components[1];
        this.patronymic = components.length == 3 ? components[2] : null;
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

        Teacher teacher = (Teacher) o;

        if (!firstName.equals(teacher.firstName)) return false;
        if (!lastName.equals(teacher.lastName)) return false;
        return patronymic != null ? patronymic.equals(teacher.patronymic) : teacher.patronymic == null;

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        return result;
    }
}
