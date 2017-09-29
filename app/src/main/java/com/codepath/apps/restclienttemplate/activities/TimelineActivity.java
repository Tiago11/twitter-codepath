package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.network.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient mClient;
    TweetAdapter mTweetAdapter;
    List<Tweet> mTweets;
    RecyclerView rvTweets;

    // Store a member variable for the listener.
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the RecyclerView.
        rvTweets = (RecyclerView) findViewById(R.id.rvTimeline);

        // Init the list (data source).
        mTweets = new ArrayList<>();

        // Construct the adapter from this data source.
        mTweetAdapter = new TweetAdapter(mTweets);

        // RecyclerView setup (layout manager, use adapter).
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call resetState() for fresh searches.
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimelineSinceId(mTweets.get(mTweets.size()-1).getUid());
            }
        };
        // Adds the scroll listener to RecyclerView.
        rvTweets.addOnScrollListener(scrollListener);

        rvTweets.setAdapter(mTweetAdapter);

        mClient = TwitterApp.getRestClient();
        populateTimelineSinceId(1);

    }

    private void populateTimelineSinceId(long id) {
        mClient.getHomeTimelineSinceId(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                // Iterate through the JSONArray.
                // For each object, deserialize the JSON Object.
                for (int i = 0; i < response.length(); i++) {
                    // Convert each object to a Tweet model.
                    // Add that tweet model to our data source.
                    // Notify the adapter that we'have added an item.
                    try {
                        Tweet tweet = Tweet.fromJson(response.getJSONObject(i));
                        mTweets.add(tweet);
                        mTweetAdapter.notifyItemInserted(mTweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
