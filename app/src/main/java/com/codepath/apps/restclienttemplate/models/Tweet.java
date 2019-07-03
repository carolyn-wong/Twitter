package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

// indicate class is Parcelable
@Parcel
public class Tweet {

    // attributes
    public String body; // text body of text
    public long uid; // database id of tweet
    public String strId;
    public User user; // user object of tweet
    public String createdAt; // timestamp of tweet
    public boolean retweeted;
    public boolean liked;

    // no arguments, empty constructor required for Parceler
    public Tweet() {}

    // deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.strId = jsonObject.getString("id_str");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public String getCreatedAt() {
        // convert timestamp to relative time
        String formattedCreatedAt = TimeFormatter.getTimeDifference(createdAt);
        return formattedCreatedAt;
    }
}
