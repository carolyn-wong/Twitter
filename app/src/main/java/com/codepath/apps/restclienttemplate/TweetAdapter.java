package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    private Activity mContext;
    // context defined as global variable so Glide in onBindViewHolder has access
    Context context;

    // pass Tweets array in constructor
    public TweetAdapter(Activity context, List<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    // for each row, inflate layout and cache references into ViewHolder

    // method invoked only when creating a new row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        // inflate layout, need to get context first
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind values based on element position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate views according to data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvScreenName.setText(String.format("@%s", tweet.user.screenName));
        holder.tvBody.setText(tweet.body);
        holder.tvCreatedAt.setText(tweet.getCreatedAt());
        holder.ivReply.setTag(tweet.uid);

        // TODO - get better resolution images by changing image link from "normal" to "bigger"
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0))
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public ImageView ivReply;
        public ImageView ivLike;
        public ImageView ivRetweet;

        // constructor takes in inflated layout
        public ViewHolder(View itemView) {
            super(itemView);

            // findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);

            itemView.setOnClickListener(this);
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // call composeTweet method from TimelineActivity
                if(mContext instanceof TimelineActivity) {
                        long replyId = (long) ivReply.getTag();
                        String userReply = String.format("%s", tvScreenName.getText().toString());
                        ((TimelineActivity) mContext).replyTweet(replyId, userReply);
                    }
                }
            });
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TIMELINE LIKE CLICK", "LIKE");
                }
            });
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TIMELINE RETWEET CLICK", "RETWEET");
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // ensure position valid (exists in view)
            if (position != RecyclerView.NO_POSITION) {
                // get tweet at position. doesn't work if class is static
                Tweet tweet = mTweets.get(position);
                Intent intent = new Intent(context, TweetDetails.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }

    // RecyclerView adapter helper methods to clear items from or add items to underlying dataset
    // clean recycler elements
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // add list of tweets - change list type depending on item type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
