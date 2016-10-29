package com.codepath.apps.mysimpletweeets.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by lramaswamy on 10/24/16.
 */


//"user": {
//              "name": "OAuth Dancer",
//              "profile_sidebar_fill_color": "DDEEF6",
//              "profile_background_tile": true,
//              "profile_sidebar_border_color": "C0DEED",
//              "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
//              "created_at": "Wed Mar 03 19:37:35 +0000 2010",
//              "location": "San Francisco, CA",
//              "follow_request_sent": false,
//              "id_str": "119476949",
//              "is_translator": false,
//              "profile_link_color": "0084B4",
//              "entities": {
//                "url": {
//                  "urls": [
//                    {
//                      "expanded_url": null,
//                      "url": "http://bit.ly/oauth-dancer",
//                      "indices": [
//                        0,
//                        26
//                      ],
//                      "display_url": null
//                    }
//                  ]
//                },
//                "description": null
//              },

    @Parcel
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public User() {}

    public static User fromJSON(JSONObject json) {
        User user = new User();

        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getCurrentUser(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void postTweet(String composedTweet) {

    }
}
