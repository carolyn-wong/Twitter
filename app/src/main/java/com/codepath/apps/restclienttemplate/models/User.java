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

        return user;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
