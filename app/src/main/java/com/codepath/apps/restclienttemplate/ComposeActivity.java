package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    EditText etCompose;
    Button btCompose;
    TextView tvCharCount;
    int charLimit = 280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // initialize twitter client
        client = TwitterApp.getRestClient(this);

        // assign views
        btCompose = (Button) findViewById(R.id.btCompose);
        etCompose = (EditText) findViewById(R.id.etCompose);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);

        // set TextWatcher for character count (total character limit enforced in xml file)
        etCompose.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // set TextView tvCharCount to current length
                    int charsLeft = charLimit - s.length();
                    tvCharCount.setText(String.valueOf(charsLeft));
                    // if 0 chars remaining, set charCount to red
                    // TODO - don't hardcode text color in here
                    // TODO - also maybe don't need so many methods in the TextWatcher??
                    if(charsLeft == 0) {
                        tvCharCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                    else {
                        tvCharCount.setTextColor(Color.parseColor("#657786"));
                    }
                }

                public void afterTextChanged(Editable s) {
                }
            });
        // set onClickListener and network request for tweet button
        btCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String composeText = etCompose.getText().toString();
                client.sendTweet(composeText, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // convert to Tweet model
                            Tweet newTweet = Tweet.fromJSON(response);
                            // create intent to send data back to TimelineActivity
                            Intent returnTweet = new Intent();
                            // pass tweet as extra serialized via Parcels.wrap(), using short name as key
                            returnTweet.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(newTweet));
                            setResult(RESULT_OK, returnTweet);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });
    }
}
