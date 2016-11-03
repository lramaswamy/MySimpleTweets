package com.codepath.apps.mysimpletweeets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweeets.R;
import com.codepath.apps.mysimpletweeets.TweetDetailsActivity;
import com.codepath.apps.mysimpletweeets.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by lramaswamy on 10/26/16.
 */

public class TweetsRecyclerAdapter extends RecyclerView.Adapter<TweetsRecyclerAdapter.ViewHolder> {

    public List<Tweet> mTweets;
    public Context context;

    public TweetsRecyclerAdapter(List<Tweet> tweets, Context context) {
        mTweets = tweets;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the individual
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, tweetView, mTweets);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the item
        Tweet tweet = mTweets.get(position);
        holder.imageView.setImageResource(android.R.color.transparent);
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvTweetBody.setText(tweet.getBody());
        holder.tvCreatedAt.setText(tweet.getCreatedAt());
        Glide.with(context)
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView ;
        TextView tvUserName;
        LinkifiedTextView tvTweetBody;
        TextView tvCreatedAt;
        List<Tweet> mTweets;
        Context mContext;

        public ViewHolder(Context context, View view, List<Tweet> tweets) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvTweetBody = (LinkifiedTextView) view.findViewById(R.id.tvTweetBody);
            tvCreatedAt = (TextView) view.findViewById(R.id.tvRelativeTime);
            mTweets = tweets;
            mContext = context;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Intent intent = new Intent(mContext, TweetDetailsActivity.class);
            intent.putExtra("tweet", Parcels.wrap(mTweets.get(getAdapterPosition())));
            mContext.startActivity(intent);
        }
    }
}
