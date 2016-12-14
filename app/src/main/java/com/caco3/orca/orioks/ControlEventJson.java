package com.caco3.orca.orioks;


import com.google.gson.annotations.SerializedName;

/**
 * JsonSchema2PoJo
 * It's internal class which maps raw response to pojo
 * It should not go out of this package.
 * App should use {@link com.caco3.orca.orioks.model.ControlEvent} instead of this class.
 * Use {@link OrioksResponseAdapter} to convert this class
 */
/*package*/ class ControlEventJson {

    @SerializedName("ball")
    /*package*/ MarkJson markJson;

    /**
     * There must be boolean, but they return "" - for false and 1 for true
     */
    @SerializedName("bonus")
    /*package*/ Object bonus;

    @SerializedName("max_ball")
    /*package*/ float maxPoints;

    @SerializedName("min_ball")
    /*package*/ float minPoints;

    @SerializedName("name")
    /*package*/ String subject;

    @SerializedName("type_name")
    /*package*/ String typeName;

    @SerializedName("week")
    /*package*/ int week;
}
