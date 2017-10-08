package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;

/**
 * Created by tiago on 10/7/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

    public UserTimelineFragment() {

    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    // Make the API call to get the users timeline since the tweet with id `id`.
    public void populateTimelineSinceId(long id) {
        String screenName = getArguments().getString("screen_name");

        // Get the handler for GET populateTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getUserTimelineSinceId(screenName, id,
                tweetHandler.getPopulateTimelineHandler(id, mTweets, mTweetAdapter, getContext()));
    }

    // Make the API call to refresh the timeline.
    public void refreshTimeline() {
        String screenName = getArguments().getString("screen_name");

        mTweetAdapter.clear();
        mTweetAdapter.notifyDataSetChanged();

        // Get the handler for GET refreshTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getUserTimelineSinceId(screenName, 1, tweetHandler.getRefreshTimelineHandler(getContext(), mTweetAdapter, mSwipeContainer));
    }
}
