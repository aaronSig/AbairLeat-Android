package com.superpixel.lurgan.abairleat.dto;

import com.firebase.client.AuthData;

/**
 * Created by Martin on 1/21/16.
 */
public class AuthStatus {
    private AuthData authData;

    public AuthStatus(AuthData authData) {
        this.authData = authData;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public boolean isLoggedIn() {
        return authData != null;
    }
}
