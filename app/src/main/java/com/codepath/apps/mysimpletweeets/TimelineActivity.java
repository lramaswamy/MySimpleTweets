package com.codepath.apps.mysimpletweeets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweeets.Adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweeets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweeets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweeets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweeets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweeets.R.id.vpPager;

public class TimelineActivity extends AppCompatActivity implements TweetCompose.OnTweetPass {

    private TwitterClient client;
    ViewPager viewPager;
    TweetsPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(vpPager);
        viewPager.setAdapter(pagerAdapter);
        //fragment = (TweetsListFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
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
    public void OnTweetPass(String tweetData) {
        String composedTweet = tweetData;
        client.postTweet(composedTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TweetsListFragment fragment = (TweetsListFragment)pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());;
                fragment.getTweetsSince();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //Launch the profile view
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    //return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //controls the order and creation of the fragments
        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return new HomeTimelineFragment();
            else if (position == 1)
                return new MentionsTimelineFragment();
            else
                return null;
        }

        //returns the Title of the tab
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
