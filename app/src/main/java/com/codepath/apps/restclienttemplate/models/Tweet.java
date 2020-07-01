package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    private static final String TAG = "Tweet_TAG";

    public String body;
    public String createdAt;
    public User user;
    public String imageMediaUrl;

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        Log.i(TAG, "before getting entities");
        JSONObject entities;
        JSONArray media;
        try {
            entities = jsonObject.getJSONObject("entities");
            media = entities.getJSONArray("media");
            JSONObject mediaObject = media.getJSONObject(0);
            tweet.imageMediaUrl = mediaObject.getString("media_url_https");
            Log.i(TAG, "media array found!");
        } catch (JSONException e) {
            Log.d(TAG, "media not found");
            e.printStackTrace();
            tweet.imageMediaUrl = null;
            return tweet;
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }


}
