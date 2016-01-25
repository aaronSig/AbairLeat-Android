package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/25/16.
 */
@EViewGroup(R.layout.view_message_element)
public class MessageElementView extends RelativeLayout {

    @ViewById(R.id.profile_image)
    protected ImageView profileImageView;
    @ViewById(R.id.participant)
    protected TextView participantView;
    @ViewById(R.id.message)
    protected TextView messageView;

    public MessageElementView(Context context) {
        super(context);
    }

    public MessageElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(MessageDTO messageDTO) {
        participantView.setText(messageDTO.getAuthor());
        messageView.setText(messageDTO.getText());
    }

}
