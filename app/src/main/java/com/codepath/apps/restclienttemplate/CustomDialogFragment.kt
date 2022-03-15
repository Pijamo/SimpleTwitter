package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class CustomDialogFragment: DialogFragment() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

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
                Toast.makeText(context, "Tweet is too long! Limit is 140 charactyers", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, tweetContent, Toast.LENGTH_SHORT).show()

                // TODO: make an api call to Twitter to publish tweet
            }
        }
    }
}