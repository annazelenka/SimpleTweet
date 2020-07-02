package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    private static View mRootView;

    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }



    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the data with the view holder
        holder.bind(tweet);

        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        mRootView.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRootView = recyclerView.getRootView();
    }

    // Clean all elements in the recycler view
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of tweets
    public void addAll(List<Tweet> newTweets) {
        tweets.addAll(newTweets);
        notifyDataSetChanged();
    }



    // Pass in the context and list of tweets

    // For each row, inflate the layout

    // Bind values based on the position of the element

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        ImageView ivImageMedia;
        TextView tvBody;
        TextView tvName;
        TextView tvScreenName;
        TextView tvRelativeTimeStamp;
        ImageButton btnExpandTweet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimeStamp = itemView.findViewById(R.id.tvRelativeTimeStamp);
            ivImageMedia = itemView.findViewById(R.id.ivImageMedia);
            btnExpandTweet = itemView.findViewById(R.id.btnExpandTweet);

            btnExpandTweet.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvRelativeTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));
            tvName.setText(tweet.user.name);
            int profileImageRadius = 120;
            Glide
                    .with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(profileImageRadius))
                    .into(ivProfileImage);

            if (tweet.imageMediaUrl != null) {
                int imageMediaRadius = 120;
                int margin = 10;
                Glide.with(context)
                        .load(tweet.imageMediaUrl)
                        .transform(new RoundedCorners(imageMediaRadius))
                        .into(ivImageMedia);
                ivImageMedia.setVisibility(View.VISIBLE);
            } else {
                ivImageMedia.setVisibility(View.GONE);
            }
        }

        // USAGE: getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014")
        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            } catch (ParseException e) {
                Log.e("TweetsAdapter", "failed to parse created_at into relative timestamp");
                e.printStackTrace();
            }

            // trim "ago" off of end
            return relativeDate.substring(0, relativeDate.length() - 4);
        }

        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();
            //make sure position is valid, ie actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailActivity.class);
                // serialize the movie using Parceler, use its short name as a key
                intent.putExtra("tweet", Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
