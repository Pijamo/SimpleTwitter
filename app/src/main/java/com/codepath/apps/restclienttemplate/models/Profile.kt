package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject

class Profile {

    var name: String = ""
    var profileImageURL: String = ""

    companion object {
        fun fromJson(jsonObject: JSONObject): Profile {
            val profile = Profile()
            profile.name = jsonObject.getString("name")
            profile.profileImageURL = jsonObject.getString("profile_image_url_https")

            return profile
        }
    }
}