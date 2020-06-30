package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // needs to be Parcelable b/c Tweet object needs to be Parcelable, and Tweet contains User
public class User {

    public String name;
    public String screenName;
    public String publicImageUrl;
    public String profileImageUrl;

    // empty constructor needed by Parceler
    public User() {
    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        return user;
    }
}
