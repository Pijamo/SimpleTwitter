package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(val tweets: MutableList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)


        //inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    //incharge of binding date to the viewholder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // Get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        // Set item view based on views and data model

        holder.tvUserName.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvTimeStamp.text = tweet.createdAt
        holder.tvRetweet.text = tweet.retweet_count.toString()
        holder.tvFav.text = tweet.favorite_count.toString()
        holder.tvHandle.text = tweet.user?.screenName

        if (tweet.user?.isVerified == true){
            holder.tvVerified.setImageResource(R.drawable.ic_verified)
        }

        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvVerified = itemView.findViewById<ImageView>(R.id.imageView)
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)
        val tvFav = itemView.findViewById<TextView>(R.id.tvFavCount)
        val tvRetweet = itemView.findViewById<TextView>(R.id.rvRetweetCount)
        val tvHandle = itemView.findViewById<TextView>(R.id.tvHandle)
    }
}