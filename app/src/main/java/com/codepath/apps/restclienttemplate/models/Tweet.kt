package com.codepath.apps.restclienttemplate.models
import android.os.Parcelable
import androidx.room.Entity;

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Long.MAX_VALUE
import kotlin.collections.ArrayList

var max_id = MAX_VALUE

@Parcelize
@Entity
data class Tweet(
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
    var replyCount: Int,
    @ColumnInfo
    var retweetCount: Int,
    @ColumnInfo
    var favoriteCount: Int,
) : Parcelable {
    companion object {
        fun fromJson(jsonObject: JSONObject, userArray: JSONArray) : Tweet {

            val publicMatrix = jsonObject.getJSONObject("public_metrics")
            val body = jsonObject.getString("text")
            val createdAt = getFormattedTimestamp(jsonObject.getString("created_at"))

            val author_id = jsonObject.getLong("author_id")

            val user = mapUser(userArray, author_id)?.let { User.fromJson(it) }

            val id = jsonObject.getLong("id")
            val replyCount = publicMatrix.getInt("reply_count")
            val retweetCount = publicMatrix.getInt("retweet_count")
            val favoriteCount = publicMatrix.getInt("like_count")

            if (id < max_id){
                max_id = id
            }

            return Tweet(body, createdAt, user, id, replyCount, retweetCount, favoriteCount)
        }

        private fun mapUser(userArray: JSONArray, authorId: Long): JSONObject? {
            var jsonObject: JSONObject? = null

            for (i in 0 until userArray.length()){
                if(userArray.getJSONObject(i).getLong("id").equals(authorId)){
                    jsonObject =  userArray.getJSONObject(i)
                }
            }
            return jsonObject
        }

        fun fromJsonArray(jsonObject: JSONObject): List<Tweet> {
            val tweets = ArrayList<Tweet>()

            val tweetArray = jsonObject.getJSONArray("data")
            val userArray = jsonObject.getJSONObject("includes").getJSONArray("users")

            for (i in 0 until  tweetArray.length()) {
                tweets.add(fromJson(tweetArray.getJSONObject(i), userArray))
            }
            return tweets
        }

        fun getFormattedTimestamp(string: String): String {
            return TimeFormatter.getTimeDifference(string)
        }
    }
}