package com.codepath.apps.restclienttemplate.models
import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey

import org.json.JSONObject

@Entity
class User{

    @ColumnInfo
    @PrimaryKey
    var user_id: Long = 0

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    var screenName: String = ""

    @ColumnInfo
    var publicImageUrl: String = ""

    @ColumnInfo
    var isVerified: Boolean = false

    companion object {
        fun fromJson(jsonObject: JSONObject): User {
            val user = User()
            user.user_id = jsonObject.getLong("id")
            user.name = jsonObject.getString("name")
            user.screenName = "@" + jsonObject.getString("screen_name")
            user.publicImageUrl = jsonObject.getString("profile_image_url_https")
            user.isVerified = jsonObject.getBoolean("verified")

            return user
        }
    }
}
