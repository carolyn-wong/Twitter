package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    // attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String location;
    public String followers;
    public String friends;
    public String description;
    public String createdAt;

    // no arguments, empty constructor for Parcelable
    public User() {}

    // deserialize JSON
    public static User fromJson(JSONObject json) throws JSONException {
        User user = new User();

        // extract values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url_https");
        user.location = json.getString("location");
        user.followers = json.getString("followers_count");
        user.friends = json.getString("friends_count");
        user.description = json.getString("description");
        user.createdAt = json.getString("created_at");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        // convert timestamp to relative time
        String formattedCreatedAt = TimeFormatter.getTimeDifference(createdAt);
        return formattedCreatedAt;
    }

}
