package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.activities.LoginActivity_;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.services.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/26/16.
 */
@EViewGroup(R.layout.view_profile)
public class ProfileView extends ScrollView {

    @Bean
    protected API api;
    @Bean
    protected ProfileService profileService;

    @ViewById(R.id.profile_image)
    protected ImageView profileImageView;

    @ViewById(R.id.status)
    protected TextView statusView;
    @ViewById(R.id.name)
    protected TextView nameView;
    @ViewById(R.id.id)
    protected TextView idView;
    @ViewById(R.id.provider)
    protected TextView providerView;


    public ProfileView(Context context) {
        super(context);
    }

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void afterViews() {
        monitorConnection();
        loadProfile();
    }

    @Click(R.id.logout)
    protected void onLogoutClicked() {
        profileService.logout();
        LoginActivity_.intent(getContext()).start();
    }

    private void monitorConnection() {
        api.firebase().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                updateConnectionStatus(connected);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadProfile() {
        ProfileDTO profile = api.getProfile();

        inflateProfilePicture(profile);
        inflateDetails(profile);
    }

    private void inflateProfilePicture(ProfileDTO profile) {
        Glide.with(getContext()).load(profile.getAvatarUrlString()).centerCrop().into(profileImageView);
    }

    private void inflateDetails(ProfileDTO profile) {
        nameView.setText(profile.getName());
        idView.setText(profile.getId());
        providerView.setText(api.getAuthData().getProvider());
    }

    @UiThread
    protected void updateConnectionStatus(boolean connected) {
        if(connected) {
            statusView.setText(R.string.connected);
        } else {
            statusView.setText(R.string.disconnected);
        }
    }

}
