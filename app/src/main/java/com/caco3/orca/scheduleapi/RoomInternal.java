package com.caco3.orca.scheduleapi;


import com.google.gson.annotations.SerializedName;

/**
 * They mean classroom
 */
/*package*/ class RoomInternal {

    /**
     * Don't know what it means
     */
    @SerializedName("Code")
    int code;

    /**
     * name of classroom
     */
    @SerializedName("Name")
    String name;

    @Override
    public String toString() {
        return "RoomInternal{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
