package com.codepath.apps.restclienttemplate.fragments;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;

/**
 * Created by tiago on 10/7/17.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    public HomeTimelineFragment() {

    }

    // Make the API call to get the users timeline since the tweet with id `id`.
    public void populateTimelineSinceId(long id) {
        // Get the handler for GET populateTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getHomeTimelineSinceId(id,
                tweetHandler.getPopulateTimelineHandler(id, mTweets, mTweetAdapter, getContext()));
    }

    // Make the API call to refresh the timeline.
    public void refreshTimeline() {

        mTweetAdapter.clear();
        mTweetAdapter.notifyDataSetChanged();

        // Get the handler for GET refreshTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getHomeTimelineSinceId(1, tweetHandler.getRefreshTimelineHandler(getContext(), mTweetAdapter, mSwipeContainer));
    }

    public void insertTweetAtTop(Tweet tweet) {
        // Get the handler for POST composeDialog.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.postTweet(tweet, tweetHandler.getComposeTweetHandler(getContext(), mTweets, mTweetAdapter, rvTweets));
    }
}
