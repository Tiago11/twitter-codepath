package com.codepath.apps.restclienttemplate.network;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.interfaces.ProgressBarListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tiago on 9/30/17.
 */

// Auxiliary class to refactor JsonHttpResponseHandler logic out of activities.
public class TweetJsonHttpResponseHandler {

    // Listener used to send data back to the activity.
    private TweetHandlerListener mListener;

    // Constructor
    public TweetJsonHttpResponseHandler() {
        this.mListener = null;
    }

    // Interface used to send data back to the activity.
    public interface TweetHandlerListener {
        public void setCurrentUser(User user);
    }

    // Set the listener.
    public void setTweetHandlerListener(TweetHandlerListener listener) {
        this.mListener = listener;
    }

    // Get a JsonHttpResponseHandler for the populateTimeline API call.
    public JsonHttpResponseHandler getPopulateTimelineHandler(final long maxId, final List<Tweet> tweets, final TweetAdapter adapter, final Context context, final ProgressBarListener progressBarListener) {
        // Start showing the progress bar.
        progressBarListener.showProgressBar();

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                List<Tweet> newTweets = Tweet.fromJsonArray(response);

                // If there is no new tweets, do nothing.
                if (newTweets.size() > 0 && newTweets.get(0).getUid() > maxId) {
                    tweets.addAll(newTweets);
                    adapter.notifyDataSetChanged();
                }

                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();

                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();

                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();

                progressBarListener.hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);

                progressBarListener.hideProgressBar();
            }
        };

        return  handler;
    }

    // Get a JsonHttpResponseHandler for the refreshTimeline API call.
    public JsonHttpResponseHandler getRefreshTimelineHandler(final Context context, final TweetAdapter adapter, final SwipeRefreshLayout swipeRefreshLayout, final ProgressBarListener progressBarListener) {
        progressBarListener.showProgressBar();

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                adapter.addAll(Tweet.fromJsonArray(response));
                swipeRefreshLayout.setRefreshing(false);
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
                progressBarListener.hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                progressBarListener.hideProgressBar();
            }
        };

        return handler;
    }

    // Get a JsonHttpResponseHandler for the composeTweet API call.
    public JsonHttpResponseHandler getComposeTweetHandler(final Context context, final List<Tweet> tweets, final TweetAdapter adapter, final RecyclerView rvTweets) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                // Get the tweet id from the response.
                try {
                    Tweet tweet = Tweet.fromJson(response);
                    tweets.add(0, tweet);
                    adapter.notifyItemInserted(0);
                    rvTweets.scrollToPosition(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }
        };

        return handler;
    }

    // Get a JsonHttpResponseHandler for the getCurrentUser API call.
    public JsonHttpResponseHandler getCurrentUserHandler(final Context context) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                try {
                    User currentUser = User.fromJson(response);
                    // Use the listener to send the current user information back to the activity.
                    if (mListener != null) {
                        mListener.setCurrentUser(currentUser);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }
        };

        return handler;
    }
}
