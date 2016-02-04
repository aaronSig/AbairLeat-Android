package com.superpixel.lurgan.abairleat.services;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.dto.ProfileRealm;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import io.realm.Realm;

/**
 * Created by Martin on 1/26/16.
 *
 * Under development ...
 */
@EBean(scope = EBean.Scope.Singleton)
public class ContactCacheService {

    private static final String LOG_TAG = "ProfileCache";

    @RootContext
    protected Context context;

    @Bean
    protected API api;

    public boolean isInitialized = false;


    @AfterInject
    protected void afterInject() {
        init();
    }

    public void init() {
        if(isInitialized) return;

        api.firebaseForCurrentUser().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    updateProfile(dataSnapshot.getValue(ProfileDTO.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        api.firebaseForCurrentUser().child("contacts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                loadProfile(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                loadProfile(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                loadProfile(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                loadProfile(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadProfile(String profileLink) {
        Firebase firebaseRef = api.firebaseFromLink(profileLink);
        if(firebaseRef != null) {
            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        updateProfile(dataSnapshot.getValue(ProfileDTO.class));
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        isInitialized = true;
    }

    public void updateProfile(ProfileDTO profile) {
        if(profile == null) {
            Log.e(LOG_TAG, "ProfileDTO = null!");
            return;
        }

        Realm realm = Realm.getInstance(context);
        ProfileRealm profileRealm = realm.where(ProfileRealm.class).equalTo("id", profile.getId()).findFirst();

        if(profileRealm == null) {
            profileRealm = realm.createObject(ProfileRealm.class);
        }

        realm.beginTransaction();

        profile.toRealm(profileRealm);

        realm.commitTransaction();
    }

//    public ProfileDTO fetchWithId(String id) {
//        Realm realm = Realm.getInstance(context);
//
//        ProfileRealm profileRealm = realm.where(ProfileRealm.class).equalTo("id", id).findFirst();
//
//        return ProfileDTO.fromRealm(profileRealm);
//    }

}
