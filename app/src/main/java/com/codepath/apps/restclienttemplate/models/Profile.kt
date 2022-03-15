package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject

data class Profile (
    var id: Int,
    var name: String,
    var profileImageURL: String,
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Profile {

            val name = jsonObject.getString("name")
            val profileImageURL = jsonObject.getString("profile_image_url_https")
            val id = jsonObject.getInt("id")

            return Profile(id, name, profileImageURL)
        }
    }
}



