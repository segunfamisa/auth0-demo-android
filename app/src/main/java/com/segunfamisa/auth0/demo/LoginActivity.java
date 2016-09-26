package com.segunfamisa.auth0.demo;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Auth0 Lock
     */
    private Lock mLock;

    /**
     * Auth0 account
     */
    private Auth0 mAuth0;

    /**
     * Log tag
     */
    private String TAG = "AuthenticationLog";

    private Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize views
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mButtonLogin.setOnClickListener(this);

        // initialize auth account
        mAuth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));

        // initialize lock
        mLock = new Lock.Builder(mAuth0, mLockCallback)
                .allowLogIn(true)
                .allowSignUp(true)
                .allowForgotPassword(true)
                .loginAfterSignUp(true)
                .build();
        mLock.onCreate(this);
    }

    private final LockCallback mLockCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Log.d(TAG, "Authentication completed:");
            Log.d(TAG, "AccessToken: " + credentials.getAccessToken());
            Log.d(TAG, "IdToken: " + credentials.getIdToken());
        }

        @Override
        public void onCanceled() {
            Snackbar.make(mButtonLogin, "Authentication canceled", Snackbar.LENGTH_INDEFINITE)
                    .show();
        }

        @Override
        public void onError(LockException error) {
            Snackbar.make(mButtonLogin, "Error: " + error.getErrorMessage(getApplicationContext()),
                    Snackbar.LENGTH_INDEFINITE).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregister the lock to prevent possible memory leaks
        if (mLock != null) {
            mLock.onDestroy(this);
            mLock = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mButtonLogin) {
            // start auth0 authentication UI
            startActivity(mLock.newIntent(this));
        }
    }
}
