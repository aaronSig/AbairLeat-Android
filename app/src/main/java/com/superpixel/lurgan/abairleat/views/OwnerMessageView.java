package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.api.API;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/26/16.
 */
@EViewGroup(R.layout.view_owner_message)
public class OwnerMessageView extends MessageView {

    @Bean
    protected API api;

    @ViewById(R.id.profile_image)
    protected ImageView profileImageView;
    @ViewById(R.id.message)
    protected TextView messageView;
    @ViewById(R.id.date)
    protected TextView dateView;

    public OwnerMessageView(Context context) {
        super(context);
    }

    public OwnerMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OwnerMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextView getMessageTextView() {
        return messageView;
    }

    @Override
    public TextView getDateTextView() {
        return dateView;
    }

    @Override
    public ImageView getProfileImageView() {
        return profileImageView;
    }

    @Override
    public API getApi() {
        return api;
    }
}
