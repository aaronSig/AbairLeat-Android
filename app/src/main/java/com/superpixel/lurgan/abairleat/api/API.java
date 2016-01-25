package com.superpixel.lurgan.abairleat.api;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/21/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class API {

    private static String LOG_TAG = "API";

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

    /*
     * INITIALIZATION
     */

    private void init(Context context) {
        Firebase.setAndroidContext(context);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        firebase = new Firebase(ApiUrl());
        firebaseVersioned = new Firebase(ApiUrl()).child(ApiVersion());
    }

    public void loadProfile(final InitializationCallback callback) {
        if(firebaseVersioned().getAuth() != null) {
            firebaseForUser(getAuthenticatedUserToken()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            profile = dataSnapshot.getValue(ProfileDTO.class);

                            Log.e(LOG_TAG, "profile loaded");
                            Log.e(LOG_TAG, profile.toString());

                            if(callback != null) {
                                callback.onInitializationCompleted(profile);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            if(callback != null) {
                                callback.onInitializationFailed(firebaseError);
                            }
                        }
                    }
            );
        }
    }

    /*
     * FIREBASE INSTANCE RETRIEVAL
     */

    public Firebase firebase() {
        return firebase;
    }

    public Firebase firebaseVersioned() {
        return firebaseVersioned;
    }

    public Firebase firebaseForUser(String id) {
        return firebaseVersioned().child("profiles").child(id);
    }

    public Firebase firebaseForCurrentUser() {
        if(profile == null) return null;

        return firebaseForUser(profile.getId());
    }

    public Firebase firebaseForConversation(String conversationId) {
        return firebaseVersioned().child("conversations").child(conversationId);
    }

    public Firebase firebaseFromLink(String link) throws IllegalArgumentException {
        if(link.startsWith("@link")) {
            String[] path = link.split(">");

            if(path[1].equals("profiles")) {
                return firebaseForUser(path[2]);
            } else if (path[1].equals("conversations")) {
                return firebaseForConversation(path[2]);
            }
        }

        throw new IllegalArgumentException("Invalid link: " + link);
    }

    /*
     * GETTERS / SETTERS
     */

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public String getAuthenticatedUserToken() {
        try {
            return firebaseVersioned().getAuth().getProviderData().get("id").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * HELPER METHODS
     */

    /**
     * Create or update a user
     *
     * @param profile profile to be created / updated
     * @param listener Firebase completion listener
     */
    public void updateProfile(ProfileDTO profile, Firebase.CompletionListener listener) {
        firebaseForUser(profile.getId()).updateChildren(profile.toMap(), listener);
    }

    /**
     * Adds or update a contact
     *
     * @param contact contact to be added / updated
     * @param listener Firebase completion listener
     */
    public void updateContact(ProfileDTO contact, Firebase.CompletionListener listener) {
        firebaseForCurrentUser().child("contacts")
                .child(linkify(contact))
                .setValue(contact.getName(), listener);
    }

    /**
     * Generate the conversation ID. Currently only supporting one to one messages!
     * You only need to provide the participating user object, current user is provided
     * by the implementation
     *
     * <br><br>
     * <b>Format: 1x[id1]-[id2]</b>
     * <br><br>
     *
     * IDs are ordered mathematically by size, smaller first.
     *
     * @param user participating user
     * @return formatted conversation ID
     */
    public String getConversationId(ProfileDTO user) {
        long participantId = Long.valueOf(user.getId());
        long myId = Long.valueOf(profile.getId());

        long largerId = Math.max(participantId, myId);
        long smallerId = Math.min(participantId, myId);

        return String.format("1x%s-%s", largerId, smallerId);
    }


    /*
     * API CONFIG VARIABLES
     */

    public static String ApiVersion() {
        return API_VERSION;
    }

    public static String ApiUrl() {
        return API_URL;
    }


    /*
     * HELPER METHODS
     */
    public static String linkify(ProfileDTO profile) {
        return String.format("@link>profiles>%s", profile.getId());
    }


    /*
     * INITIALIZATION CALLBACK
     */

    public interface InitializationCallback {
        void onInitializationCompleted(ProfileDTO profile);
        void onInitializationFailed(FirebaseError firebaseError);
    }
}
