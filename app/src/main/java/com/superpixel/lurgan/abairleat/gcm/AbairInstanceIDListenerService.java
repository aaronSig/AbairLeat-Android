package com.superpixel.lurgan.abairleat.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.superpixel.lurgan.abairleat.api.API;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

/**
 * Created by Martin on 2/2/16.
 */
@EService
public class AbairInstanceIDListenerService extends InstanceIDListenerService {

    @Bean
    protected API api;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
