package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
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
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment, container)
    }


    private fun sendData(tweetID: Long) {

        val result = tweetID

        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCompose = view.findViewById(R.id.etTweetCompose)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        etCompose.addTextChangedListener(object : TextWatcher {

            var max_length = 280;

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                tvCount.text = max_length.toString()
                // Fires right before text is changing

            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                this.max_length -= 1;

                tvCount.text = max_length.toString()
            }
        })

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

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet!")

                        sendData(json.jsonObject.getLong("id"))
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