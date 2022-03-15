package com.codepath.apps.restclienttemplate.models
import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey

import org.json.JSONObject

@Entity
data class User(
    @ColumnInfo
    @PrimaryKey
    var user_id: Long,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var screenName: String,

    @ColumnInfo
    var publicImageUrl: String,

    @ColumnInfo
    var isVerified: Boolean,
){
    companion object {
        fun fromJson(jsonObject: JSONObject): User {

            val user_id = jsonObject.getLong("id")
            val name = jsonObject.getString("name")
            val screenName = "@" + jsonObject.getString("screen_name")
            val publicImageUrl = jsonObject.getString("profile_image_url_https")
            val isVerified = jsonObject.getBoolean("verified")

            return User(user_id, name, screenName, publicImageUrl, isVerified)
        }
    }
}
