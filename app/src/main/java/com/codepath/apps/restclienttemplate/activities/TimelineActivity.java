package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.adapters.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.network.ConnectivityChecker;
import com.codepath.apps.restclienttemplate.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.restclienttemplate.network.TwitterClient;

public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeDialogListener {

    User mCurrentUser;
    ViewPager vpPager;
    TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Setup Toolbar.
        setupToolbar();

        // Check connectivity.
        ConnectivityChecker cc = new ConnectivityChecker(this);
        cc.checkConnectivity();

        // Get and set the current user.
        TwitterClient client = TwitterApp.getRestClient();
        TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler();
        handler.setTweetHandlerListener(new TweetJsonHttpResponseHandler.TweetHandlerListener() {
            @Override
            public void setCurrentUser(User user) {
                mCurrentUser = user;
            }
        });
        client.getCurrentUser(handler.getCurrentUserHandler(this));

        vpPager = (ViewPager) findViewById(R.id.viewpager);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(tweetsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                // Start the compose dialog fragment to create a new Tweet.
                showComposeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Show the "compose new tweet dialog" and pass to it the current user information.
    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(mCurrentUser);

        composeDialogFragment.show(fm, "fragment_compose_tweet");
    }


    // Receive the tweet created in the composeDialog, and make the API call to POST it.
    @Override
    public void onSendTweetComposeDialog(Tweet tweet) {
        // Get the HomeTimeline fragment.
        HomeTimelineFragment fragment = (HomeTimelineFragment) tweetsPagerAdapter.getRegisteredFragment(0);
        // Change the pager to show the HomeTimeline fragment.
        vpPager.setCurrentItem(0, true);
        // Insert the tweet at the top of the timeline.
        fragment.insertTweetAtTop(tweet);
    }

    // Setup the toolbar.
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}
