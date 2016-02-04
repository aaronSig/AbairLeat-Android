package com.superpixel.lurgan.abairleat.util;

import android.support.v4.util.LruCache;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

/**
 * Created by Martin on 1/26/16.
 */
public class ProfileCache {
    private static final String LOG_TAG = "ProfileCache";

    private static LruCache<String, ProfileDTO> cache = new LruCache<>(1000);

    public static ProfileDTO get(String id) {
        return cache.get(id);
    }

    public static void set(ProfileDTO profile) {
        if(profile != null) {
            Log.d(LOG_TAG, "adding profile");
            Log.d(LOG_TAG, profile.toString());

            cache.put(profile.getId(), profile);
        }
    }

    public static void remove(String id) {

    }

    public static void init(final API api) {
        api.firebaseForCurrentUser().child("contacts").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        loadProfile(dataSnapshot.getKey(), api);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        loadProfile(dataSnapshot.getKey(), api);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        // can still keep cache even if friend removed ...
                        loadProfile(dataSnapshot.getKey(), api);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        loadProfile(dataSnapshot.getKey(), api);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );
    }

    private static void loadProfile(String link, API api) {
        api.firebaseFromLink(link).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                set(dataSnapshot.getValue(ProfileDTO.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}