package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvLocation;
    private TextView tvFollowCount;
    private TextView tvFriendsCount;
    private TextView tvCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User profileUser = (User) Parcels.unwrap(getIntent().getParcelableExtra("user_details"));

        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvFollowCount = (TextView) findViewById(R.id.tvFollowCount);
        tvFriendsCount = (TextView) findViewById(R.id.tvFriendsCount);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);

        tvUserName.setText(profileUser.name);
        tvScreenName.setText(profileUser.screenName);
        tvLocation.setText(profileUser.location);
        tvFollowCount.setText(profileUser.followers);
        tvFriendsCount.setText(profileUser.friends);;
        tvCreatedAt.setText(profileUser.getCreatedAt());
    }
}
