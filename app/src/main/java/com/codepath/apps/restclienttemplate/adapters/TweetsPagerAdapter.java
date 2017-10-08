package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;
import com.codepath.apps.restclienttemplate.utils.SmartFragmentStatePagerAdapter;

/**
 * Created by tiago on 10/7/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private String tabTitles[] = new String[] { "Home", "Mentions" };
    private int tabItemsCount = 2;
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabItemsCount;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        }else if (position == 1) {
            return new MentionsTimelineFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on position.
        return tabTitles[position];
    }
}
