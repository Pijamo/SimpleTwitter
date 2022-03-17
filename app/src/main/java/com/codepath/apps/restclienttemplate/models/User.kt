package com.codepath.apps.restclienttemplate.models
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

import org.json.JSONObject

@Parcelize
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
) : Parcelable {
    companion object {
        fun fromJson(jsonObject: JSONObject): User {

            val user_id = jsonObject.getLong("id")
            val name = jsonObject.getString("name")
            val screenName = "@" + jsonObject.getString("username")
            val publicImageUrl = jsonObject.getString("profile_image_url")
            val isVerified = jsonObject.getBoolean("verified")

            return User(user_id, name, screenName, publicImageUrl, isVerified)
        }
    }
}
