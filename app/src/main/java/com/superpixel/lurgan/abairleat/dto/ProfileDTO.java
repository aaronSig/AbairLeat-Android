package com.superpixel.lurgan.abairleat.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 1/21/16.
 */
public class ProfileDTO {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String avatarUrl;

    public ProfileDTO() {}

    public ProfileDTO(String id, String name, String firstName, String lastName, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> vals = new HashMap<>(5);

        vals.put("id", getId());
        vals.put("name", getName());
        vals.put("firstName", getFirstName());
        vals.put("lastName", getLastName());
        vals.put("avatarUrlString", getAvatarUrl());

        return vals;
    }

    public static ProfileDTO fromFacebookProfile(JSONObject profile) {
        return new ProfileDTO(
                getStringNullSafe(profile, "id"),
                getStringNullSafe(profile, "name"),
                getStringNullSafe(profile, "first_name"),
                getStringNullSafe(profile, "last_name"),

                getPictureUrlNullSafe(profile)
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
}
