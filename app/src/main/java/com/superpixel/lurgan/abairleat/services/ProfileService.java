package com.superpixel.lurgan.abairleat.services;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.AuthStatus;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Martin on 1/20/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ProfileService implements Firebase.AuthStateListener, Firebase.AuthResultHandler {

    private static final String LOG_TAG = "ProfileService";

    private Firebase firebaseRef;
    private AccessToken accessToken;

    private boolean profileListenerAttached = false;

    @Bean
    protected API api;

    @AfterInject
    protected void afterInject() {
        firebaseRef = api.firebaseVersioned().child("profiles");
        firebaseRef.addAuthStateListener(this);
    }

    public void logout() {
        firebaseRef.unauth();
        LoginManager.getInstance().logOut();

        notifyProfileStateChange(new ProfileDTO());
        notifyAuthStatusChange(new AuthStatus(null));
    }

    public void facebookLogin(AccessToken accessToken) {
        if(accessToken != null) {
            this.accessToken = accessToken;
            firebaseRef.authWithOAuthToken("facebook", accessToken.getToken(), this);
        }
    }

    public boolean isAuthenticated() {
        AuthData auth;
        if((auth = api.firebaseVersioned().getAuth()) != null) {
            attachProfileChangedListener(auth);
            return true;
        }

        return false;
    }

    private void attachProfileChangedListener(AuthData auth) {
        if(profileListenerAttached == false) {
            api.firebaseForUser(auth.getUid()).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ProfileDTO profile = dataSnapshot.getValue(ProfileDTO.class);
                            notifyProfileStateChange(profile);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    }
            );
            profileListenerAttached = true;
        }
    }

    private void profileGraphRequest(final AuthData authData) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        ProfileDTO profile = ProfileDTO.fromFacebookProfile(object);
                        storeProfile(profile, authData);
                    }
                }
        );

        Bundle params = new Bundle();
        params.putString("fields", "name,first_name,last_name,picture");

        request.setParameters(params);
        request.executeAsync();
    }

    private void storeProfile(final ProfileDTO profile, final AuthData authData) {
        api.updateProfile(
                profile,
                new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if(firebaseError == null) {
                            attachProfileChangedListener(authData);

                            api.setProfile(profile);
                            notifyProfileStateChange(profile);
                        } else {
                            Log.e(LOG_TAG, firebaseError.toString());
                        }
                    }
                }
        );
    }

    private void notifyProfileStateChange(ProfileDTO profile) {
        if(profile != null) {
            EventBus.getDefault().post(profile);
        }
    }

    private void notifyAuthStatusChange(AuthStatus authStatus) {
        if(authStatus != null) {
            EventBus.getDefault().post(authStatus);
        }
    }

    /*
     * AuthStateListener
     */
    @Override
    public void onAuthStateChanged(AuthData authData) {
        if(authData != null) {
            Log.d(LOG_TAG, "authData != null");
        } else {
            Log.d(LOG_TAG, "authData == null");
        }

        notifyAuthStatusChange(new AuthStatus(authData));
    }

    /*
     * AuthResultHandler
     */
    @Override
    public void onAuthenticated(AuthData authData) {
        profileGraphRequest(authData);
//        notifyAuthStatusChange(new AuthStatus(authData));
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        notifyAuthStatusChange(new AuthStatus(null));
    }
}
