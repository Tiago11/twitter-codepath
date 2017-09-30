package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by tiago on 9/26/17.
 */

@Parcel
public class User {

    // List all the attributes.
    public String name;
    public Long uid;
    public String screenName;
    public String profileImageUrl;

    public User() {
        // Empty constructor needed by the Parceler library.
    }

    // Deserialize the JSON.
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.name = jsonObject.getString("name");
        if (jsonObject.has("id")) {
            user.uid = jsonObject.getLong("id");
        }
        if (jsonObject.has("screen_name")) {
            user.screenName = jsonObject.getString("screen_name");
        }
        user.profileImageUrl = jsonObject.getString("profile_image_url");

        return user;
    }

    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}
