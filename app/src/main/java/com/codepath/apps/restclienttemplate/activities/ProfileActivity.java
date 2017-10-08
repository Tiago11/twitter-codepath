package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.restclienttemplate.network.TwitterClient;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient();
        TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler();
        // Set listener to get the current user.
        handler.setTweetHandlerListener(new TweetJsonHttpResponseHandler.TweetHandlerListener() {
            @Override
            public void setCurrentUser(User user) {
                populateUserHeadline(user);
            }
        });

        String screenName = getIntent().getStringExtra("screen_name");

        if (TextUtils.isEmpty(screenName)) {
            client.getCurrentUser(handler.getCurrentUserHandler(this));
        } else {
            client.getUserInfo(screenName, handler.getCurrentUserHandler(this));
        }

        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();
    }

    private void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " followers");
        tvFollowing.setText(user.getFollowingCount() + " following");

        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
