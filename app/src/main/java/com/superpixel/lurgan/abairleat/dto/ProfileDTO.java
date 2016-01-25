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

    public ProfileDTO() {}

    public ProfileDTO(String id, String name, String firstName, String lastName, String avatarUrlString) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrlString = avatarUrlString;
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

    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> vals = new HashMap<>(5);

        vals.put("id", getId());
        vals.put("name", getName());
        vals.put("firstName", getFirstName());
        vals.put("lastName", getLastName());
        vals.put("avatarUrlString", getAvatarUrlString());

        return vals;
    }


    public static ProfileDTO fromFacebookProfile(JSONObject profile) {
        if(profile == null) return null;

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
