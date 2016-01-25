package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.activities.ConversationActivity_;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/25/16.
 */
@EViewGroup(R.layout.view_contacts_element)
public class ContactsElementView extends RelativeLayout implements View.OnClickListener {

    @Bean
    protected API api;

    @ViewById(R.id.name)
    protected TextView nameView;
    @ViewById(R.id.link)
    protected TextView linkView;
    @ViewById(R.id.profile_image)
    protected ImageView profileImageView;

    private ProfileDTO profile;

    public ContactsElementView(Context context) {
        super(context);
    }

    public ContactsElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void bind(String name, String contactLink) {
        nameView.setText(name);
        linkView.setText(contactLink);

        setOnClickListener(this);

        Firebase firebase = api.firebaseFromLink(contactLink);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(ProfileDTO.class);
                loadProfile(profile);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadProfile(ProfileDTO profile) {
        Glide.with(getContext()).load(profile.getAvatarUrlString()).into(profileImageView);
    }

    @Override
    public void onClick(View v) {
        if(profile != null) {
            String conversationId = api.getConversationId(profile);

            ConversationActivity_.intent(getContext()).conversationId(conversationId).start();
        }
    }
}
