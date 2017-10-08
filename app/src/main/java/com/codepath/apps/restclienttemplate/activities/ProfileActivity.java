package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.interfaces.ProgressBarListener;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.restclienttemplate.network.TwitterClient;

public class ProfileActivity extends AppCompatActivity implements ProgressBarListener{

    TwitterClient client;
    private MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup Toolbar.
        setupToolbar();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " " + getString(R.string.followers));
        tvFollowing.setText(user.getFollowingCount() + " " + getString(R.string.following));

        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }


    // Setup the toolbar.
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    // Implement ProgressBar interface method.
    @Override
    public void showProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    // Implement ProgressBar interface method.
    @Override
    public void hideProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }
}
