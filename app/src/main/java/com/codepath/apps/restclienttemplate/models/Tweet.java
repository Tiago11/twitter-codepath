package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.utils.ParseRelativeDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 9/26/17.
 */

@Parcel
public class Tweet {

    // List of attributes.
    public String body;
    public long uid;
    public User user;
    public String createdAt;

    public Tweet() {
        // Empty constructor needed by the Parceler library.
    }

    public Tweet(String body) {
        this.body = body;
    }

    public Tweet(JSONObject jsonObject) {
        try {

            this.body = jsonObject.getString("text");
            this.uid = jsonObject.getLong("id");
            this.createdAt = jsonObject.getString("created_at");
            this.user = User.fromJson(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Deserialize the JSON
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // Extract all the values from JSON.
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        return tweet;
    }

    // Static factory method to create a collection of tweets.
    public static List<Tweet> fromJsonArray(JSONArray array) {
        ArrayList<Tweet> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Tweet(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return ParseRelativeDate.getRelativeTimeAgo(createdAt);
    }
}
