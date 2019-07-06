package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.ProfileActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TweetDetails extends AppCompatActivity {

    private Tweet tweet;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvCreatedAt;
    private TextView tvBody;
    private ImageView ivProfileImage;
    private ImageView ivRetweet;
    private ImageView ivLike;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApp.getRestClient(this);

        // find views
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivLike = (ImageView) findViewById(R.id.ivLike);

        // unwrap tweet passed in intent
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("TweetDetailsActivity", "Showing details for tweet");

        // set views
        tvUserName.setText(tweet.user.name);
        tvScreenName.setText(String.format("@%s", tweet.user.screenName));
        tvCreatedAt.setText(tweet.getCreatedAt());
        tvBody.setText(tweet.body);

        Glide.with(getBaseContext())
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                // check retweet status
                if (ivRetweet.isSelected()) {
                    client.unRetweet(tweet.strId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            ivRetweet.setSelected(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                else {
                    client.Retweet(tweet.strId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            ivRetweet.setSelected(true);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                // check like status
                if (ivLike.isSelected()) {
                    client.unlikeTweet(tweet.strId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            ivLike.setSelected(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                else {
                    client.likeTweet(tweet.strId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            ivLike.setSelected(true);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });

        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TweetDetails.this, ProfileActivity.class);
                i.putExtra("user_details", Parcels.wrap(tweet.user));
                startActivity(i);
            }
        });
    }
}
