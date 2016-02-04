package com.superpixel.lurgan.abairleat.dto;

import io.realm.RealmObject;

/**
 * Created by Martin on 2/4/16.
 */
public class ProfileRealm extends RealmObject {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String avatarUrlString;

    private String androidNotificationToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrlString() {
        return avatarUrlString;
    }

    public void setAvatarUrlString(String avatarUrlString) {
        this.avatarUrlString = avatarUrlString;
    }

    public String getAndroidNotificationToken() {
        return androidNotificationToken;
    }

    public void setAndroidNotificationToken(String androidNotificationToken) {
        this.androidNotificationToken = androidNotificationToken;
    }
}
