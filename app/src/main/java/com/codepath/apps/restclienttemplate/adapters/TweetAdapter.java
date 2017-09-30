package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

/**
 * Created by tiago on 9/26/17.
 */

public class TweetAdapter extends  RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    private Context mContext;

    // Pass in the Tweets array into the constructor.
    public TweetAdapter(List<Tweet> tweets) {
        this.mTweets = tweets;
    }

    // For each row, inflate the layout and cache the references into the viewHolder.

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }


    // Bind the values based on the position of the element.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the item according to the position.
        Tweet tweet = mTweets.get(position);

        // Populate the views according to this data.
        holder.tvUsername.setText(tweet.getUser().getName());
        holder.tvScreenName.setText(tweet.getUser().getScreenName());
        holder.tvBody.setText(tweet.getBody());
        holder.tvCreatedAt.setText(tweet.getCreatedAt());

        Glide.with(mContext).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Methods for SwipeRefresh.
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweets) {
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);

            // Perform findViewById lookups.

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
        }
    }

}
