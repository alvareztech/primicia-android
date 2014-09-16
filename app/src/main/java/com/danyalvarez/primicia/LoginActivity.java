package com.danyalvarez.primicia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danyalvarez.primicia.util.Constants;

public class LoginActivity extends ActionBarActivity {

    private TextView mMarqueeTextView;

    private Button mTwitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMarqueeTextView = (TextView) findViewById(R.id.marqueeTextView);
        mMarqueeTextView.setSelected(true);
        mTwitterLoginButton = (Button) findViewById(R.id.twitterLoginButton);
        mTwitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTwitter(view);
            }
        });

    }


    public void loginTwitter(final View view) {
        Log.i(Constants.TAG_DEBUG, "Pressed loginTwitter");

        view.setEnabled(false);

        /*
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                Log.e(Constants.TAG_DEBUG, err.getMessage());
                if (user == null) {
                    Log.d(Constants.TAG_DEBUG, "Uh oh. The user cancelled the Twitter login.");
                    String token = ParseTwitterUtils.getTwitter().getAuthToken();
                    String tokenSecret = ParseTwitterUtils.getTwitter().getAuthTokenSecret();
                    Log.i(Constants.TAG_DEBUG, "> " + token + " " + tokenSecret);

                    view.setEnabled(true);
                } else if (user.isNew()) {
                    Log.d(Constants.TAG_DEBUG, "User signed up and logged in through Twitter!");

                    String token = ParseTwitterUtils.getTwitter().getAuthToken();
                    String tokenSecret = ParseTwitterUtils.getTwitter().getAuthTokenSecret();
                    Util.setTwitterAuthToken(token, tokenSecret, LoginActivity.this);

                    Log.i(Constants.TAG_DEBUG, "> " + token + " " + tokenSecret);

                    startMain();
                } else {
                    Log.d(Constants.TAG_DEBUG, "User logged in through Twitter!");

                    String token = ParseTwitterUtils.getTwitter().getAuthToken();
                    String tokenSecret = ParseTwitterUtils.getTwitter().getAuthTokenSecret();
                    Util.setTwitterAuthToken(token, tokenSecret, LoginActivity.this);

                    Log.i(Constants.TAG_DEBUG, "> " + token + " " + tokenSecret);

                    startMain();
                }
            }
        });
        */
    }

    public void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
