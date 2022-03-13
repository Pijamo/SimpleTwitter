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
class Tweet {

    @ColumnInfo
    var body: String = ""

    @ColumnInfo
    var createdAt: String = ""

    @ColumnInfo
    var user: User? = null

    @ColumnInfo
    @PrimaryKey
    var id = 0L

    @ColumnInfo
    var retweet_count: Int = 0

    @ColumnInfo
    var favorite_count: Int = 0

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = getFormattedTimestamp(jsonObject.getString("created_at"))
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.id = jsonObject.getLong("id")
            tweet.retweet_count = jsonObject.getInt("retweet_count")
            tweet.favorite_count = jsonObject.getInt("favorite_count")

            if (tweet.id < max_id){
                max_id = tweet.id
            }

            return tweet
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