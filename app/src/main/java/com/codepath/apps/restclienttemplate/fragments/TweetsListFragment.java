package com.codepath.apps.restclienttemplate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.interfaces.ProgressBarListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.network.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 10/7/17.
 */

public abstract class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {

    TweetAdapter mTweetAdapter;
    List<Tweet> mTweets;
    RecyclerView rvTweets;

    TwitterClient mClient;
    ProgressBarListener mProgressBarListener;

    // Store a member variable for the swipe refresh container.
    SwipeRefreshLayout mSwipeContainer;

    // Store a member variable for the listener.
    EndlessRecyclerViewScrollListener mScrollListener;

    // Store a member variable for the layout manager.
    LinearLayoutManager mLinearLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = TwitterApp.getRestClient();

        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(mTweets, this);

        populateTimelineSinceId(1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        // Setup RecyclerView, TweetsAdapter and Tweets collection.
        setupTweetsRecyclerView(v);

        // Setup SwipeContainer for refreshing the timeline.
        setupSwipeContainer(v);

        return v;
    }

    public abstract void populateTimelineSinceId(long id);

    public abstract void refreshTimeline();

    // Find references and set the TweetsAdapter, TweetsRecyclerView and Tweets collection.
    private void setupTweetsRecyclerView(View v) {
        // Find the RecyclerView.
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTimeline);

        // RecyclerView setup (layout manager, use adapter).
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(mLinearLayoutManager);

        // Retain an instance so that you can call resetState() for fresh searches.
        mScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimelineSinceId(mTweets.get(mTweets.size()-1).getUid());
            }
        };

        // Adds the scroll listener to RecyclerView.
        rvTweets.addOnScrollListener(mScrollListener);

        rvTweets.setAdapter(mTweetAdapter);
    }


    // Setup the SwipeContainer to manage the timeline refresh on swipe.
    private void setupSwipeContainer(View v) {

        // Get the swipe container view.
        mSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

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
    }


    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = mTweets.get(position);
        //Toast.makeText(getContext(), tweet.body, Toast.LENGTH_SHORT).show();
    }

    // Store the listener (activity) that will fire the progressBar events.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ProgressBarListener) {
            mProgressBarListener = (ProgressBarListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " has to implement the ProgressBarListener interface.");
        }
    }
}
