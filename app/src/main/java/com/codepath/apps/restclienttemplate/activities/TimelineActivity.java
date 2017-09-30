package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.fragments.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.network.ConnectivityChecker;
import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.restclienttemplate.network.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeDialogListener {

    TwitterClient mClient;
    User mCurrentUser;
    TweetAdapter mTweetAdapter;
    List<Tweet> mTweets;

    RecyclerView rvTweets;

    // Store a member variable for the listener.
    private EndlessRecyclerViewScrollListener mScrollListener;

    // Store a member variable for the swipe refresh container.
    private SwipeRefreshLayout mSwipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Check connectivity.
        ConnectivityChecker cc = new ConnectivityChecker(this);
        cc.checkConnectivity();

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
        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimelineSinceId(mTweets.get(mTweets.size()-1).getUid());
            }
        };
        // Adds the scroll listener to RecyclerView.
        rvTweets.addOnScrollListener(mScrollListener);

        rvTweets.setAdapter(mTweetAdapter);

        // Get the swipe container view.
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading.
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });
        // Configure the refreshing colors.
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright);

        mClient = TwitterApp.getRestClient();

        // Set the current user.
        getCurrentUser();

        // Populate the RecyclerView with the tweets from the currentUser timeline.
        populateTimelineSinceId(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                // Start the compose dialog fragment.
                showComposeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(mCurrentUser);

        composeDialogFragment.show(fm, "fragment_compose_tweet");
    }

    private void getCurrentUser() {
        // Get the handler for GET currentUser
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();
        // Set listener to get the current user.
        tweetHandler.setTweetHanlerListener(new TweetJsonHttpResponseHandler.TweetHandlerListener() {
            @Override
            public void setCurrentUser(User user) {
                mCurrentUser = user;
            }
        });

        mClient.getCurrentUser(tweetHandler.getCurrentUserHandler(TimelineActivity.this));

    }

    private void populateTimelineSinceId(long id) {
        // Get the handler for GET populateTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getHomeTimelineSinceId(id,
                tweetHandler.getPopulateTimelineHandler(mTweets, mTweetAdapter, TimelineActivity.this));
    }

    private void refreshTimeline() {

        mTweetAdapter.clear();
        mTweetAdapter.notifyDataSetChanged();

        // Get the handler for GET refreshTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getHomeTimelineSinceId(1, tweetHandler.getRefreshTimelineHandler(TimelineActivity.this, mTweetAdapter, mSwipeContainer));
    }

    @Override
    public void onSendTweetComposeDialog(Tweet tweet) {
        // Get the handler for POST composeDialog.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.postTweet(tweet, tweetHandler.getComposeTweetHandler(TimelineActivity.this, mTweets, mTweetAdapter, rvTweets));
    }

}
