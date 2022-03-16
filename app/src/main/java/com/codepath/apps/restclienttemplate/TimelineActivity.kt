package com.codepath.apps.restclienttemplate

import EndlessRecyclerViewScrollListener
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding
import com.codepath.apps.restclienttemplate.models.Profile
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

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTimelineBinding = DataBindingUtil.setContentView(this, R.layout.activity_timeline)

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
            android.R.color.holo_red_light)

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

        val args = Bundle()

        profileImage(binding, args)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            showComposeDialog(args)
        }

        populateHomeTimeline()
    }

    fun showComposeDialog(args: Bundle) {
        val fm: FragmentManager = supportFragmentManager

        val composeFragment: ComposeFragment? =
            ComposeFragment.newInstance(args)

        composeFragment?.arguments = args

        fm.setFragmentResultListener("requestKey", this) { requestKey, bundle ->

            val tweet = bundle.get("tweet") as Tweet

            tweets.add(0, tweet)

            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)

            Log.i(TAG, bundle.toString())
        }

        composeFragment?.show(fm, "fragment_edit_name")
    }


    fun profileImage(binding: ActivityTimelineBinding, args: Bundle) {
        client.getUserDetails(object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON)  {
                Log.i("User", "Success")

                val profile = Profile.fromJson(json.jsonObject)

                args.putString("profileUrl", profile.profileImageURL)

                binding.profile = profile
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

                    Log.i("idTest", "Max_id = $max_id")

                    swipeContainer.isRefreshing = false
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

                        Log.i("loadMore", "Max_id = $max_id")

                        swipeContainer.isRefreshing = false
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
        const val TAG = "TimelineActivity"
    }
}