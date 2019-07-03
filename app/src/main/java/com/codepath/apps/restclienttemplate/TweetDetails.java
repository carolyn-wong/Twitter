package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetails extends AppCompatActivity {

    private Tweet tweet;
    private TextView tvUserName;
    private TextView tvCreatedAt;
    private TextView tvBody;
    private ImageView ivProfileImage;
    private ImageView ivRetweet;
    private ImageView ivLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivLike = (ImageView) findViewById(R.id.ivLike);

        // unwrap tweet passed in intent
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("TweetDetailsActivity", "Showing details for tweet");

        tvUserName.setText(tweet.getUser().getName());
        tvCreatedAt.setText(tweet.getCreatedAt());
        tvBody.setText(tweet.getBody());

        Glide.with(getBaseContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);


    }
}
