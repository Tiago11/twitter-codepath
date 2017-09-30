package com.codepath.apps.restclienttemplate.network;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
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

public class TweetJsonHttpResponseHandler {

    private TweetHandlerListener mListener;

    public TweetJsonHttpResponseHandler() {
        this.mListener = null;
    }

    public interface TweetHandlerListener {
        public void setCurrentUser(User user);
    }

    public void setTweetHanlerListener(TweetHandlerListener listener) {
        this.mListener = listener;
    }

    public JsonHttpResponseHandler getPopulateTimelineHandler(final List<Tweet> tweets, final TweetAdapter adapter, final Context context) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                /*
                // Iterate through the JSONArray.
                // For each object, deserialize the JSON Object.
                for (int i = 0; i < response.length(); i++) {
                    // Convert each object to a Tweet model.
                    // Add that tweet model to our data source.
                    // Notify the adapter that we'have added an item.
                    try {
                        Tweet tweet = Tweet.fromJson(response.getJSONObject(i));
                        tweets.add(tweet);
                        adapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                */
                tweets.addAll(Tweet.fromJsonArray(response));
                adapter.notifyDataSetChanged();
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }
        };

        return  handler;
    }

    public JsonHttpResponseHandler getRefreshTimelineHandler(final Context context, final TweetAdapter adapter, final SwipeRefreshLayout swipeRefreshLayout) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
/*
                List<Tweet> newTweets = new ArrayList<>();
                // Iterate through the JSONArray.
                // For each object, deserialize the JSON Object.
                for (int i = 0; i < response.length(); i++) {
                    // Convert each object to a Tweet model.
                    // Add that tweet model to our data source.
                    // Notify the adapter that we'have added an item.
                    try {
                        Tweet tweet = Tweet.fromJson(response.getJSONObject(i));
                        newTweets.add(tweet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
*/
                adapter.addAll(Tweet.fromJsonArray(response));
                swipeRefreshLayout.setRefreshing(false);
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

                // Check connectivity.
                ConnectivityChecker cc = new ConnectivityChecker(context);
                cc.checkConnectivity();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }
        };

        return handler;
    }

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

    public JsonHttpResponseHandler getCurrentUserHandler(final Context context) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                try {
                    User currentUser = User.fromJson(response);
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
