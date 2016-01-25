package com.superpixel.lurgan.abairleat.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.ContactsFirebaseRecyclerAdapter;
import com.superpixel.lurgan.abairleat.services.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/20/16.
 */
@EActivity(R.layout.activity_dashboard)
public class DashboardActivity extends BaseActivity {
    private static final String LOG_TAG = "DashboardActivity";

    @Bean
    protected ProfileService profileService;
    @Bean
    protected ContactsFirebaseRecyclerAdapter contactsAdapter;

    @ViewById(R.id.contacts)
    protected RecyclerView contactsView;


    @AfterViews
    protected void afterViews() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        contactsView.setLayoutManager(layoutManager);
        contactsView.setAdapter(contactsAdapter);
    }


    @Click(R.id.logout)
    protected void onLogoutClicked() {
        profileService.logout();
        LoginActivity_.intent(this).start();
        finish();
    }
}
