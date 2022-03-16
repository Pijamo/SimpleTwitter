package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import okhttp3.Headers


class ComposeFragment() : DialogFragment(){

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var ivProfileImage: ImageView

    lateinit var closebutton: ImageButton

    lateinit var client: TwitterClient
    lateinit var profileURl: String

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
        return inflater.inflate(R.layout.dialog_fragment, container)
    }

    interface EditNameDialogListener {
    }

    fun sendBackResult() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCompose = view.findViewById(R.id.etTweetCompose)
        btnTweet = view.findViewById(R.id.btnTweet)
        profileURl = arguments?.getString("profileUrl").toString()
        closebutton = view.findViewById(R.id.btnClose)

        ivProfileImage = view.findViewById(R.id.ivProfileImage)

        Glide.with(view.context).load(profileURl).fitCenter().transform(RoundedCornersTransformation(250,0)).into(ivProfileImage)

        client = context?.let { TwitterApplication.getRestClient(it) }!!

        closebutton.setOnClickListener{
            dismiss()
        }

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
                        //TODO("Not yet implemented")

                        dismiss()
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
    }


    companion object {

        fun newInstance(args: Bundle): ComposeFragment? {
            val frag = ComposeFragment()
            var title = "This is a title"
            args.putString("title", title)
            frag.arguments = args
            return frag
        }

        val TAG = "ComposeActivity"
    }
}