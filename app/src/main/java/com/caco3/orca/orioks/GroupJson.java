package com.caco3.orca.orioks;

import com.google.gson.annotations.SerializedName;

/**
 * JsonSchema2PoJo
 * It's internal class which maps raw response to pojo
 * It should not go out of this package.
 */
/*package*/ class GroupJson {

    @SerializedName("name")
    /*package*/ String name;
}
