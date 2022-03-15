package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeFragment: DialogFragment() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCompose = view.findViewById(R.id.etTweetCompose)
        btnTweet = view.findViewById(R.id.btnTweet)

        client = context?.let { TwitterApplication.getRestClient(it) }!!

        btnTweet.setOnClickListener {

            //Grab the context of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            //1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(context, "Empty tweets not allowed", Toast.LENGTH_SHORT).show()
                // Look into displaying SnackBar message
            } else

            //2. make sure the tweet is under character count
            if (tweetContent.length > 140) {
                Toast.makeText(context, "Tweet is too long! Limit is 140 characters", Toast.LENGTH_LONG).show()
            } else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                        Log.i(TAG, "Successfully published tweet!")
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                })
            }
        }
    } companion object {
        val TAG = "ComposeActivity"
    }
}