package com.superpixel.lurgan.abairleat.gcm;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.api.API;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;

/**
 * Created by Martin on 2/2/16.
 */
@EBean
public class GcmRegistrationService {

    private static final String LOG_TAG = "GcmRegistrationService";

    @Bean
    protected API api;

    @RootContext
    protected Context context;

    @Background
    public void register() {
        if(isAvailable()) {
            api.setGcmToken(getGcmToken());
        }
    }

    private String getGcmToken() {
        try {
            InstanceID instanceID = InstanceID.getInstance(context);
            String token = instanceID.getToken(
                    context.getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );

            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isAvailable() {
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(availability == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Log.d(LOG_TAG, "Play services unavailable");
        }

        return false;
    }
}
