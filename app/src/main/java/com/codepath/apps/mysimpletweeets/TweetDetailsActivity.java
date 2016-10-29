package com.codepath.apps.mysimpletweeets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweeets.Adapters.LinkifiedTextView;
import com.codepath.apps.mysimpletweeets.models.Tweet;

import org.parceler.Parcels;

/**
 * Created by lramaswamy on 10/28/16.
 */

public class TweetDetailsActivity extends AppCompatActivity{

    public TweetDetailsActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_details);
        Intent intent = getIntent();
        Tweet tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));
        populateView(tweet);
    }

    private void populateView(Tweet tweet) {
        ImageView imageViewRetweet = (ImageView) findViewById(R.id.imageRetweet) ;
        imageViewRetweet.setImageResource(R.drawable.twitter_retweet);

        TextView tvRetweetCount = (TextView) findViewById(R.id.tvRetweets);
        tvRetweetCount.setText(Integer.toString(tweet.getRetweets()));

        TextView favoriteCount = (TextView) findViewById(R.id.tvFavorite);
        favoriteCount.setText(Integer.toString(tweet.getFavorites()));

        TextView tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);
        tvRelativeTime.setText(tweet.getCreatedAt());

        ImageView imageView = (ImageView) findViewById(R.id.ivProfileImage);
        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .into(imageView);

        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(tweet.getUser().getName());

        LinkifiedTextView tvTweetBody = (LinkifiedTextView) findViewById(R.id.tvTweetBody);
        tvTweetBody.setText(tweet.getBody());

        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvScreenName.setText(tweet.getUser().getScreenName());
    }


}
