package com.superpixel.lurgan.abairleat.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Martin on 1/21/16.
 */
//@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileDTO extends FirebaseDTO {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String avatarUrlString;

    private String androidNotificationToken;

    public ProfileDTO() {}

    public ProfileDTO(String id, String name, String firstName, String lastName, String avatarUrlString) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrlString = avatarUrlString;
    }

    public ProfileDTO(String id, String name, String firstName, String lastName, String avatarUrlString, String androidNotificationToken) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrlString = avatarUrlString;

        this.androidNotificationToken = androidNotificationToken;
    }

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

    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> vals = new HashMap<>(5);

        vals.put("id", getId());
        vals.put("name", getName());
        vals.put("firstName", getFirstName());
        vals.put("lastName", getLastName());
        vals.put("avatarUrlString", getAvatarUrlString());

        vals.put("androidNotificationToken", getAndroidNotificationToken());

        return vals;
    }


    public static ProfileDTO fromFacebookProfile(JSONObject profile) {
        if(profile == null) return null;

        return new ProfileDTO(
                getStringNullSafe(profile, "id"),
                getStringNullSafe(profile, "name"),
                getStringNullSafe(profile, "first_name"),
                getStringNullSafe(profile, "last_name"),

                getPictureUrlNullSafe(profile),

                getStringNullSafe(profile, "android")
        );
    }

    private static String getStringNullSafe(JSONObject jObj, String fieldName) {
        try {
            return jObj.getString(fieldName);
        } catch (JSONException e) {}

        return "";
    }

    private static String getPictureUrlNullSafe(JSONObject jObj) {
        try {
            return jObj.getJSONObject("picture").getJSONObject("data").getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static ProfileDTO fromRealm(ProfileRealm profileRealm) {
        ProfileDTO profile = new ProfileDTO();

        profile.setId(profileRealm.getId());
        profile.setName(profileRealm.getName());
        profile.setFirstName(profileRealm.getFirstName());
        profile.setLastName(profileRealm.getLastName());
        profile.setAvatarUrlString(profileRealm.getAvatarUrlString());
        profile.setAndroidNotificationToken(profileRealm.getAndroidNotificationToken());

        return profile;
    }

    public ProfileRealm toRealm(ProfileRealm profileRealm) {
        if(profileRealm == null) {
            profileRealm = new ProfileRealm();
        }

        profileRealm.setId(getId());
        profileRealm.setName(getName());
        profileRealm.setFirstName(getFirstName());
        profileRealm.setLastName(getLastName());
        profileRealm.setAvatarUrlString(getAvatarUrlString());
        profileRealm.setAndroidNotificationToken(getAndroidNotificationToken());

        return profileRealm;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatarUrlString='" + avatarUrlString + '\'' +
                '}';
    }
}
