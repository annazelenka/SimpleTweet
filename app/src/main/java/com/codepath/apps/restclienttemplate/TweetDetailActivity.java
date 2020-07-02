package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";

    Tweet tweet;

    TextView tvName;
    TextView tvScreenName;
    TextView tvRelativeTimestamp;
    TextView tvBody;
    ImageView ivProfileImage;
    ImageView ivMediaImage;
    ImageButton btnTweet;
    ImageButton btnFavorite;

    TwitterClient client;

    boolean canRetweet;
    boolean canFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivMediaImage = findViewById(R.id.ivMediaImage);
        tvBody = findViewById(R.id.tvBody);
        tvRelativeTimestamp = findViewById(R.id.tvRelativeTimeStamp);
        btnTweet = findViewById(R.id.btnTweet);
        btnFavorite = findViewById(R.id.btnFavorite);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvRelativeTimestamp.setText(getParsedDate(tweet.createdAt));


        int radius = 120;


        // set up rounded images for profile image and (potentially) image in tweet
        Glide
                .with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(radius))
                .into(ivProfileImage);

        radius = 50;
        if (tweet.imageMediaUrl != null) {
            int imageMediaRadius = 70;
            int margin = 10;
            Glide.with(this)
                    .load(tweet.imageMediaUrl)
                    .transform(new RoundedCorners(radius))
                    .into(ivMediaImage);
            ivMediaImage.setVisibility(View.VISIBLE);
        } else {
            ivMediaImage.setVisibility(View.GONE);
        }

        // color "retweet" and "favorite" buttons
        btnFavorite.setColorFilter(R.color.inline_action_like);
        btnTweet.setColorFilter(R.color.inline_action_retweet);

        // handle API calls
        client = TwitterApp.getRestClient(this);
        canRetweet = true;
        canFavorite = true;
        setupRetweeting();
        setupFavoriting();
    }



    public String getParsedDate(String rawJsonDate) {
        SimpleDateFormat twitterFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        SimpleDateFormat newFormat = new SimpleDateFormat("h:mm a, EEEE MMMM d, yyyy");

        Date date;
        String formattedDate = "";
        try {
            date = twitterFormat.parse(rawJsonDate);
            formattedDate = newFormat.format(date);
        } catch (ParseException e) {
            Log.e("TweetsAdapter", "failed to parse created_at into relative timestamp");
            e.printStackTrace();
        }

        // trim "ago" off of end
        return formattedDate;
    }

    public void setupRetweeting() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make API call to Twitter to retweet
                if (canRetweet) {
                    client.retweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to retweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "retweeted " + tweet.id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnTweet.setBackgroundResource(R.drawable.ic_vector_retweet);
                            btnTweet.setColorFilter(R.color.inline_action_retweet_pressed);
                            canRetweet = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to retweet", throwable);
                        }
                    });
                } else { // must unretweet
                    client.unretweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to unretweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "unretweeted " + tweet.id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnTweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                            btnTweet.setColorFilter(R.color.inline_action_retweet);
                            canRetweet = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to unretweet", throwable);
                        }
                    });

                }

            }
        });
    }

    public void setupFavoriting() {
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make API call to Twitter to retweet
                if (canFavorite) {
                    client.favorite(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to favorite");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "favorited " + tweet.id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
                            btnFavorite.setColorFilter(R.color.inline_action_like_pressed);
                            canFavorite = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to favorite", throwable);
                        }
                    });
                } else { // must unretweet
                    client.unretweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to unfavorite");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "unfavorited " + tweet.id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                            btnFavorite.setColorFilter(R.color.inline_action_like);
                            canFavorite = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to unfavorite", throwable);
                        }
                    });

                }

            }
        });
    }
}