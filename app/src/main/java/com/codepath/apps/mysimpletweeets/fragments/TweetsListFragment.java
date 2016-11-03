package com.codepath.apps.mysimpletweeets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweeets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweeets.Listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweeets.R;
import com.codepath.apps.mysimpletweeets.TwitterClient;
import com.codepath.apps.mysimpletweeets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lramaswamy on 10/31/16.
 */

public abstract class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener rvScrollListener;
    LinearLayoutManager linearLayoutManager;
    private TweetsRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    TwitterClient client;
    long sinceID;
    long maxID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_layout, parent, false);
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        rvTweets.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweetsSince();
                swipeContainer.setRefreshing(false);
            }
        });

        rvScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweetsBeyond();
            }
        };
        rvTweets.addOnScrollListener(rvScrollListener);
        return view;
    }

    public List<Tweet> getTweetsList() {
        return tweets;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        //Create the adapter
        adapter = new TweetsRecyclerAdapter(tweets, getActivity());
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, getActivity());
    }

    public void addAll(List<Tweet> tweet) {
        if(sinceID == 0 && maxID == 0) {
            tweets.clear();
            tweets.addAll(tweet);
        } else {
            if (maxID != -1 && maxID != 0)
                tweets.addAll(tweets.size(), tweet);
            if (sinceID != -1 && sinceID !=0)
                tweets.addAll(0, tweet);
        }
        adapter.notifyDataSetChanged();
    }

    public abstract void getTweetsSince();
    public abstract void getTweetsBeyond();
}
