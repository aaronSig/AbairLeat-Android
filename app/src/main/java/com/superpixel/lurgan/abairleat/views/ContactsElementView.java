package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.activities.ConversationActivity_;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/25/16.
 */
@EViewGroup(R.layout.view_contacts_element)
public class ContactsElementView extends FrameLayout implements View.OnClickListener {

    @Bean
    protected API api;

    @ViewById(R.id.name)
    protected TextView nameView;
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
        Glide.with(getContext()).load(profile.getAvatarUrlString())
                .asBitmap()
                .centerCrop()
                .listener(glideRequestListener)
                .into(profileImageView);
    }

    @UiThread
    protected void setPalette(Palette palette) {
        nameView.setBackgroundColor(
                palette.getLightVibrantColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
        );

        nameView.setTextColor(
                palette.getDarkVibrantColor(getContext().getResources().getColor(R.color.colorPrimary))
        );
    }

    @Override
    public void onClick(View v) {
        if(profile != null) {
            String conversationId = api.getConversationId(profile);

            ConversationActivity_.intent(getContext()).conversationId(conversationId).start();
        }
    }

    private RequestListener glideRequestListener = new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            if(resource instanceof Bitmap) {
                setPalette(Palette.from((Bitmap) resource).generate());
            }
            return false;
        }
    };
}
