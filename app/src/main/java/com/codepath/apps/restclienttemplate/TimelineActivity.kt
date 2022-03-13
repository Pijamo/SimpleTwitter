package com.codepath.apps.restclienttemplate

import EndlessRecyclerViewScrollListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.models.max_id
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException


class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient

    lateinit var rvTweets: RecyclerView

    lateinit var adapter: TweetsAdapter

    lateinit var swipeContainer: SwipeRefreshLayout

    val tweets: MutableList<Tweet> = mutableListOf()

    var test: String = ""

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timline)

        client = TwitterApplication.getRestClient(this)
        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")

            tweets.clear()

            adapter.notifyDataSetChanged()

            scrollListener?.resetState()

            populateHomeTimeline()
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        val linearLayoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager = linearLayoutManager
        rvTweets.adapter = adapter


        rvTweets.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNextDataFromApi(page)
            }
        })

        rvTweets.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                linearLayoutManager.orientation
            )
        )



        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        profileImage()

        populateHomeTimeline()
    }

    fun profileImage() {
        client.getUserDetails(object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON)  {
                Log.i("User", "Success")

                test = json.jsonObject.getString("profile_image_url_https")

                val profilePhoto = findViewById<ImageView>(R.id.ivUserProfile)

                val toolbar = findViewById<View>(R.id.toolbar) as Toolbar

                Glide.with(toolbar).load(test).into(profilePhoto)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i("User", "onFailure $statusCode")
            }

        })
    }

    fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess!")

                val jsonArray = json.jsonArray

                try {
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()

                    Log.i("idTest", "Max_id = ${max_id.toString()}")

                    swipeContainer.setRefreshing(false)
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception $e")
                }
            }


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

        })
    }

    fun loadNextDataFromApi(offset: Int) {
        Log.i("loadMore", "ScrollEntered")
        client.getHomeTimeline(
            object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                    Log.i("loadMore", "Success 2")

                    val jsonArray = json.jsonArray

                    try {
                        val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                        tweets.addAll(listOfNewTweetsRetrieved)
                        adapter.notifyDataSetChanged()

                        Log.i("loadMore", "Max_id = ${max_id.toString()}")

                        swipeContainer.setRefreshing(false)
                    } catch (e: JSONException) {
                        Log.e("loadMore", "JSON Exception $e")
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.i("loadMore", "onFailure 2 $statusCode")
                }

            },
            max_id)
    }

    companion object {
        val TAG = "TimelineActivity"
    }
}