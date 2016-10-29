package com.codepath.apps.mysimpletweeets;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweeets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweeets.Listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweeets.models.Tweet;
import com.codepath.apps.mysimpletweeets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements TweetCompose.OnTweetPass {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener rvScrollListener;
    LinearLayoutManager linearLayoutManager;
    private TweetsRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private static int TOTAL_ITEMS_COUNT = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tweets = new ArrayList<>();

        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);

        //Create the adapter
        adapter = new TweetsRecyclerAdapter(tweets, this);
        rvTweets.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweetsSince();
            }
        });
        client = (TwitterClient)TwitterClient.getInstance(TwitterClient.class, this.getApplicationContext());
        populateTimeline(1, -1, TOTAL_ITEMS_COUNT);
        rvScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweetsBeyond();
                //populateTimeline(1, -1, TOTAL_ITEMS_COUNT);

            }
        };

        rvTweets.addOnScrollListener(rvScrollListener);
    }

    private void getTweetsSince() {
        long sinceID = tweets.get(0).getUid();
        populateTimeline(sinceID, -1, TOTAL_ITEMS_COUNT);
    }

    private void getTweetsBeyond() {
        long maxID = tweets.get(tweets.size() - 1).getUid();
        populateTimeline(-1, maxID, TOTAL_ITEMS_COUNT);
    }

    //Sends an API request to get the timeline JSON
    //Fill in the listview by creating the individual tweet objects from the big JSON
    private void populateTimeline(final long sinceID, final long maxID, int totalItemsCount) {
        client.getHomeTimeline(sinceID, maxID, totalItemsCount, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                List<Tweet> moreTweets = Tweet.fromJSONArray(json);
                if(sinceID != -1)
                    tweets.addAll(0, moreTweets);
                if(maxID != -1)
                    tweets.addAll(tweets.size(), moreTweets);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void composeNewTweet(MenuItem item) {

        client.getCurrentUserName(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User currentUser = User.getCurrentUser(response);
                FragmentManager fm = getSupportFragmentManager();
                TweetCompose composeDialog = TweetCompose.newInstance("Compose new tweet");
                composeDialog.setUserName(currentUser.getName());
                composeDialog.setScreenName(currentUser.getScreenName());
                composeDialog.setProfileURL(currentUser.getProfileImageUrl());
                composeDialog.show(fm, "tweet_compose_fragment");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public void OnTweetPass(String tweetData) {
        String composedTweet = tweetData;
        client.postTweet(composedTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getTweetsSince();
            }
        });

    }
}
