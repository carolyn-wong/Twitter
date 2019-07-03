package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    // populate action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // handle all action buttons in single method (if adding more items)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle clicks on action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // initialize client, adapter, and views
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private SwipeRefreshLayout swipeContainer;

    // variable for endless scroll listener
    private EndlessRecyclerViewScrollListener scrollListener;

    // initialize progress bar footer
    ProgressBar progressBarFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // only call setContentView once at the top
        setContentView(R.layout.activity_timeline);

        // initialize twitter client
        client = TwitterApp.getRestClient(this);
        // find RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // initialize ArrayList (data source)
        tweets = new ArrayList<>();
        // construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        // set adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline(0L);

        // retain instance so can call "resetStates" for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i("SCROLL", "SCROLLLLLLLLLL");
                Log.i("Scroll again", "scroll again");
                Long maxTweetId = getMaxId();
                populateTimeline(maxTweetId);
            }
        };
        // add scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        // look up swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // set up refresh listener that triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(0L);
            }
        });
        // configure refreshing colors
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
    }

    // populate Twitter timeline
    private void populateTimeline(final Long maxId) {
        // create anonymous class to handle response from network
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // clear out old items before appending in new ones

                if(maxId == 0L) {
                    tweetAdapter.clear();
                }

                // iterate through JSON array response
                // for each entry, deserialize JSON object
                for(int i = 0; i < response.length(); i++) {
                    try {
                        // convert each object to Tweet model
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        // add Tweet model to data source
                        tweets.add(tweet);
                        // notify adapter that item was added
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
                // on successful reload, signal that refresh has completed
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }
        });
    }

    // Compose Activity
    // create request code for compose activity
    private final int COMPOSE_CODE = 1;
    // call compose activity using intents
    public void composeTweet() {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // new composed tweet
        if(resultCode == RESULT_OK && requestCode == COMPOSE_CODE) {
            Tweet newTweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
            tweets.add(0, newTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    // get id of the oldest tweet
    private long getMaxId() {
        int tweetsSize = tweets.size();
        if(tweetsSize == 0) {
            return 0;
        }
        else {
            Tweet oldest = tweets.get(tweets.size() - 1);
            return oldest.getUid();
        }
    }

    // Set up footer progress bar
    public void setupProgressFooter() {
        // find RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // inflate footer
        View footer = getLayoutInflater().inflate(R.layout.footer_progress, null);
        progressBarFooter = (ProgressBar) footer.findViewById(R.id.pbFooter);
    }
}
