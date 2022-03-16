package com.codepath.apps.restclienttemplate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding
import com.codepath.apps.restclienttemplate.models.Tweet


class TweetsAdapter(val tweets: MutableList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
//        val inflater = LayoutInflater.from(context)
//
//
//        //inflate our item layout
//        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        val view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    //incharge of binding date to the viewholder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {

        // Get the data model based on the position
        val tweet = tweets[position]

        Log.i("tweet", "$tweet.toString()")

        holder.binding.tweet = tweet
        holder.binding.executePendingBindings()

    }

    //Tells the viewholder how many views to create
    //return number of tweets
    override fun getItemCount(): Int {
        return tweets.size
    }

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Since the layout was already inflated within onCreateViewHolder(), we
        // can use this bind() method to associate the layout variables
        // with the layout.
        var binding: ItemTweetBinding = ItemTweetBinding.bind(itemView)
    }
}