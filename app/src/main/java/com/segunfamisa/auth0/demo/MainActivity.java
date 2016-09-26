package com.segunfamisa.auth0.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.UserProfile;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private String mAccessToken;
    private String mRefreshToken;
    private String mIdToken;

    private Auth0 mAuth0;

    private static final String TAG = "Profile";

    private PrefUtils mPrefs;

    private TextView mTextName;
    private TextView mTextEmail;
    private TextView mTextLocation;
    private TextView mTextBio;
    private ImageView mImageProfile;
    private ImageView mImageFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        mTextName = (TextView) findViewById(R.id.text_name);
        mTextEmail = (TextView) findViewById(R.id.text_email);
        mTextLocation = (TextView) findViewById(R.id.text_country);
        mTextBio = (TextView) findViewById(R.id.text_bio);
        mImageProfile = (ImageView) findViewById(R.id.image_avatar);
        mImageFood = (ImageView) findViewById(R.id.image_food);

        // init auth0
        mAuth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));

        // retrieve saved credentials
        mPrefs = new PrefUtils(this);
        mAccessToken = mPrefs.getAccessToken();
        mRefreshToken = mPrefs.getRefreshToken();
        mIdToken = mPrefs.getIdToken();

        if (mIdToken != null) {
            // user is logged in
            validateToken();
        } else {
            // user is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void validateToken() {
        AuthenticationAPIClient client = new AuthenticationAPIClient(mAuth0);
        client.tokenInfo(mIdToken)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        Log.d(TAG, "User profile loaded");
                        Log.d(TAG, payload.getName());

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update ui
                                updateUI(payload);
                            }
                        });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });
    }

    private void updateUI(UserProfile profile) {
        if (profile != null) {
            // load avatar
            Glide.with(this)
                    .load(profile.getPictureURL())
                    .into(mImageProfile);

            // load food
            Glide.with(this)
                    .load("https://upload.wikimedia.org/wikipedia/commons/4/48/Ugali_%26_Sukuma_Wiki.jpg")
                    .into(mImageFood);

            // set name
            mTextName.setText(profile.getName());

            // set email
            mTextEmail.setText(profile.getEmail());

            if (profile.getExtraInfo().containsKey("description")) {
                // set bio
                mTextBio.setText(String.format(getString(R.string.text_bio),
                        profile.getExtraInfo().get("description").toString()));
            }

            if (profile.getExtraInfo().containsKey("location")) {
                // set location
                mTextLocation.setText(String.format(getString(R.string.text_location),
                        profile.getExtraInfo().get("location").toString()));
            }
        }
    }

    private void clearCredentialsAndLogout() {
        mPrefs.clearCredentials();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                clearCredentialsAndLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
