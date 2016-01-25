package com.superpixel.lurgan.abairleat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.FirebaseError;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.AuthStatus;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.services.FriendsService;
import com.superpixel.lurgan.abairleat.services.ProfileService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {

    private static final String LOG_TAG = "LoginActivity";

    @ViewById(R.id.login_button)
    protected LoginButton facebookLoginButton;

    private CallbackManager callbackManager;

    @Bean
    protected ProfileService profileService;
    @Bean
    protected FriendsService friendsService;
    @Bean
    protected API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @AfterInject
    protected void afterInject() {
        initFacebook();
    }

    @AfterViews
    protected void afterViews() {
        facebookLoginButton.setReadPermissions("user_friends");
        facebookLoginButton.registerCallback(callbackManager, this);
    }

    public void onEvent(ProfileDTO profile) {
        if(profileService.isAuthenticated()) {
            loginComplete();
        } else {
            log(Log.ERROR, "not logged in");
        }
    }

    public void onEvent(AuthStatus authStatus) {

    }

    private void checkLogin() {
        if(profileService.isAuthenticated()) {
            loginComplete();
        }
    }

    private void loginComplete() {
        friendsService.sync();
        api.loadProfile(new API.InitializationCallback() {
            @Override
            public void onInitializationCompleted(ProfileDTO profile) {
                DashboardActivity_.intent(LoginActivity.this).start();
                finish();
            }

            @Override
            public void onInitializationFailed(FirebaseError firebaseError) {
                Log.e(LOG_TAG, firebaseError.toString());
            }
        });
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                checkLogin();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        Log.d(LOG_TAG, "FB SDK initialized");
    }

    /*
     * FacebookCallback
     */
    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        String token = accessToken.getToken();

        log(Log.DEBUG, token);

        profileService.facebookLogin(accessToken);
    }

    @Override
    public void onCancel() {
        log(Log.DEBUG, "FB login cancelled");
    }

    @Override
    public void onError(FacebookException error) {
        log(Log.DEBUG, error.getMessage());
    }

    @Override
    protected String getLogTag() {
        return "LoginActivity";
    }
}
