package com.codepath.apps.restclienttemplate.fragments;

import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;

/**
 * Created by tiago on 10/7/17.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    // MentionsTimelineFragment is a subclass of TweetsListFragment and implements the parent's abstract methods.
    public MentionsTimelineFragment() {

    }

    // Make the API call to get the users timeline since the tweet with id `id`.
    public void populateTimelineSinceId(long id) {

        // Get the handler for GET populateTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getMentionsTimelineSinceId(id,
                tweetHandler.getPopulateTimelineHandler(id, mTweets, mTweetAdapter, getContext(), mProgressBarListener));
    }

    // Make the API call to refresh the timeline.
    public void refreshTimeline() {

        mTweetAdapter.clear();
        mTweetAdapter.notifyDataSetChanged();

        // Get the handler for GET refreshTimeline.
        TweetJsonHttpResponseHandler tweetHandler = new TweetJsonHttpResponseHandler();

        mClient.getMentionsTimelineSinceId(1, tweetHandler.getRefreshTimelineHandler(getContext(), mTweetAdapter, mSwipeContainer, mProgressBarListener));
    }
}
