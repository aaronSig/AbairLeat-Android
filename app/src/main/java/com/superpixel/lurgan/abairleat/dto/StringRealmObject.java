package com.superpixel.lurgan.abairleat.dto;

import io.realm.RealmObject;

/**
 * Created by Martin on 2/3/16.
 */
public class StringRealmObject extends RealmObject {
    private String value;

    public StringRealmObject() {
    }

    public StringRealmObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
