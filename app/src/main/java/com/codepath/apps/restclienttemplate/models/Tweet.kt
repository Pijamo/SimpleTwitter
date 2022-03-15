package com.codepath.apps.restclienttemplate.models
import androidx.room.Entity;

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Long.MAX_VALUE
import java.util.*
import kotlin.collections.ArrayList

var max_id = MAX_VALUE

@Entity
data class Tweet (
    @ColumnInfo
    var body: String,

    @ColumnInfo
    var createdAt: String,

    @ColumnInfo
    var user: User?,

    @ColumnInfo
    @PrimaryKey
    var id: Long,

    @ColumnInfo
    var retweetCount: Int,

    @ColumnInfo
    var favoriteCount: Int,
){
    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {

            val body = jsonObject.getString("text")
            val createdAt = getFormattedTimestamp(jsonObject.getString("created_at"))
            val user = User.fromJson(jsonObject.getJSONObject("user"))
            val id = jsonObject.getLong("id")
            val retweetCount = jsonObject.getInt("retweet_count")
            val favoriteCount = jsonObject.getInt("favorite_count")

            if (id < max_id){
                max_id = id
            }

            return Tweet(body, createdAt, user, id, retweetCount, favoriteCount)
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until  jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

        fun getFormattedTimestamp(string: String): String {
            return TimeFormatter.getTimeDifference(string)
        }
    }
}