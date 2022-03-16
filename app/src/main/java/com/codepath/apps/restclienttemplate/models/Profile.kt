package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import org.json.JSONObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile (
    var id: Int,
    var name: String,
    var profileImageURL: String,
) : Parcelable {
    companion object {
        fun fromJson(jsonObject: JSONObject): Profile {

            val name = jsonObject.getString("name")
            val profileImageURL = jsonObject.getString("profile_image_url_https")
            val id = jsonObject.getInt("id")

            return Profile(id, name, profileImageURL)
        }
    }
}



