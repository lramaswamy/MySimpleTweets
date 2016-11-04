package com.codepath.apps.mysimpletweeets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.mysimpletweeets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lramaswamy on 10/31/16.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline(1, -1);
    }

    //Sends an API request to get the timeline JSON
    //Fill in the listview by creating the individual tweet objects from the big JSON
    void populateTimeline(final long sinceID, final long maxID) {
        client.getHomeTimeline(sinceID, maxID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                List<Tweet> moreTweets = Tweet.fromJSONArray(json);
                if(maxID != -1 && getTweetsList().size() > 0) //if atleast one result is there, then remove the first one since it could be a duplicate.
                    moreTweets.remove(0);
                addAll(moreTweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
