package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.util.CropCircleTransformation;
import com.superpixel.lurgan.abairleat.services.ContactCacheService;
import com.superpixel.lurgan.abairleat.util.ProfileCache;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 1/26/16.
 */
public abstract class MessageView extends RelativeLayout {

    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd MM yyyy");
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("HH:mm");

    public MessageView(Context context) {
        super(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(MessageDTO messageDTO) {


        ProfileDTO profile;

        if(messageDTO.getAuthor().equals(getApi().getProfile().getId())) {
            profile = getApi().getProfile();
        } else {
            profile = ProfileCache.get(messageDTO.getAuthor());
        }

        if(showProfilePic()) {
            Glide.with(getContext()).load(profile.getAvatarUrlString())
                    .asBitmap()
                    .centerCrop()
                    .transform(new CropCircleTransformation(getContext()))
                    .into(getProfileImageView());
        }

        if(showDate()) {
            getDateTextView().setText(
                    shouldUseLongDate(messageDTO.getDate())
                            ? getDateFormat().format(messageDTO.getDate())
                            : getShortDateFormat().format(messageDTO.getDate())
            );
        }

        getTextProcessor().insert(messageDTO.getText(), getMessageTextView());
    }

    public boolean showDate() {
        return true;
    }

    public boolean showProfilePic() {
        return true;
    }

    public SimpleDateFormat getDateFormat() {
        return defaultDateFormat;
    }

    public SimpleDateFormat getShortDateFormat() {
        return shortDateFormat;
    }

    public ChatTextProcessor getTextProcessor() {
        return new ChatTextProcessor() {
            @Override
            public void insert(String text, TextView textView) {
                textView.setText(text);
            }
        };
    }

    public boolean shouldUseLongDate(Date date) {
        return !DateUtils.isToday(date.getTime());
    }

    public abstract TextView getMessageTextView();
    public abstract TextView getDateTextView();
    public abstract ImageView getProfileImageView();

    public abstract API getApi();

    public abstract class ChatTextProcessor {
        public abstract void insert(String text, TextView textView);
    }

}
