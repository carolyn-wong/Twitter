package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

        String composeText = getIntent().getStringExtra("content");
        etCompose.setText(composeText);
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
                    if(charsLeft == 0) {
                        tvCharCount.setTextColor(getResources().getColor(R.color.medium_red));
                    }
                    else {
                        tvCharCount.setTextColor(getResources().getColor(R.color.medium_gray));
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
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

                client.sendTweet(composeText, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            // send back to Timeline Activity
                            Tweet newTweet = Tweet.fromJSON(response);
                            Intent returnTweet = new Intent();
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
