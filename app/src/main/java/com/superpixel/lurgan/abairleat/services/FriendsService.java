package com.superpixel.lurgan.abairleat.services;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.adapters.ContactsFirebaseRecyclerAdapter;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 1/25/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FriendsService {
    private static final String LOG_TAG = "FriendsService";

    @Bean
    protected API api;
    @Bean
    protected ProfileService profileService;

    public void sync() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(response.getError() != null) {
                            Log.e(LOG_TAG, response.toString());
                            return;
                        }

                        if(object != null) {
                            Log.d(LOG_TAG, object.toString());
                            extractFriends(object);
                        } else {
                            Log.d(LOG_TAG, "no friends found");
                        }

                        Log.d(LOG_TAG, response.toString());
                    }
                }
        );

        Bundle params = new Bundle();
        params.putString("fields", "name,first_name,last_name,picture.width(500)");

        request.setParameters(params);
        request.setGraphPath("/me/friends");

        request.executeAsync();
    }

    private void extractFriends(JSONObject object) {
        try {
            final JSONArray data = object.getJSONArray("data");

            for(int i=0; i<data.length(); i++) {
                final ProfileDTO profile = ProfileDTO.fromFacebookProfile(data.getJSONObject(i));

                if(profile != null) {
                    Log.d(LOG_TAG, profile.toString());
                    Firebase profileRef = api.firebaseForUser(profile.getId());

                    profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            updateProfile(profile);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile(final ProfileDTO profile) {
        api.updateProfile(profile, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Log.e(LOG_TAG, "Profile updated");
                updateContact(profile);
            }
        });
    }

    private void updateContact(ProfileDTO contact) {
        api.updateContact(contact, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Log.e(LOG_TAG, "Contact updated");
            }
        });
    }
}
