package com.superpixel.lurgan.abairleat.activities;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.services.ProfileService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Martin on 1/20/16.
 */
@EActivity(R.layout.activity_dashboard)
public class DashboardActivity extends BaseActivity {
    @Bean
    protected ProfileService profileService;

    @Click(R.id.logout)
    protected void onLogoutClicked() {
        profileService.logout();
        LoginActivity_.intent(this).start();
        finish();
    }
}
