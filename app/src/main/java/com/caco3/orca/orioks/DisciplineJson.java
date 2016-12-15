package com.caco3.orca.orioks;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * JsonSchema2PoJo
 * It's internal class which maps raw response to pojo
 * It should not go out of this package.
 */
/*package*/ class DisciplineJson {

    @SerializedName("itog_name")
    /*package*/ String assessmentType;

    @SerializedName("kms")
    /*package*/ ControlEventJson[] controlEvents;

    @SerializedName("name")
    /*package*/ String name;

    @SerializedName("preps")
    /*package*/ Map<Integer, TeacherJson> teacherMap;

    /**
     * This value is a key for {@link OrioksResponseJson#departments} department
     * associated with this discipline
     */
    @SerializedName("id_dis")
    int disciplineId;
}
