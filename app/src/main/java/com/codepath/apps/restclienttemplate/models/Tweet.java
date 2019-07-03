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
    public User user; // user object of tweet
    public String createdAt; // timestamp of tweet

    // no arguments, empty constructor required for Parceler
    public Tweet() {}

    // deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }
}
