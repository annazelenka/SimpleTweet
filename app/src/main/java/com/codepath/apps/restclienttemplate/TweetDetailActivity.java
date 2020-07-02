package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TweetDetailActivity extends AppCompatActivity {

    Tweet tweet;

    TextView tvName;
    TextView tvScreenName;
    TextView tvRelativeTimestamp;
    TextView tvBody;
    ImageView ivProfileImage;
    ImageView ivMediaImage;


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
}