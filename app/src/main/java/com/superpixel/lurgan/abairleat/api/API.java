package com.superpixel.lurgan.abairleat.api;

import android.content.Context;

import com.firebase.client.Firebase;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/21/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class API {

    private static String API_VERSION = "v1-android";
    private static String API_URL = "https://abair-leat.firebaseio.com/";

    private Firebase firebase;
    private Firebase firebaseVersioned;

    private ProfileDTO profile;

    @RootContext
    protected Context context;

    @AfterInject
    protected void afterInject() {
        init(context);
    }

    private void init(Context context) {
        Firebase.setAndroidContext(context);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        firebase = new Firebase(ApiUrl());
        firebaseVersioned = new Firebase(ApiUrl()).child(ApiVersion());
    }

    public Firebase firebase() {
        return firebase;
    }

    public Firebase firebaseVersioned() {
        return firebaseVersioned;
    }

    public Firebase firebaseForUser(String id) {
        return firebaseVersioned().child("profiles").child(id);
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public static String ApiVersion() {
        return API_VERSION;
    }

    public static String ApiUrl() {
        return API_URL;
    }

}
