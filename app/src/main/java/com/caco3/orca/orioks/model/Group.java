package com.caco3.orca.orioks.model;

import com.caco3.orca.util.Preconditions;

import java.io.Serializable;

/**
 * A model of student group in university
 */
public class Group implements Serializable {
    /**
     * String representation of group name
     */
    private final String name;

    private static final long serialVersionUID = 89787643113464L;

    public Group(String name) {
        this.name = Preconditions.checkNotNull(name, "name == null");
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return name != null ? name.equals(group.name) : group.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
