package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.activities.ConversationActivity_;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ConversationMetadataDTO;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.util.CropCircleTransformation;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Martin on 1/28/16.
 */
@EViewGroup(R.layout.view_conversation_element)
public class ConversationElementView extends RelativeLayout implements View.OnClickListener {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @ViewById(R.id.date)
    protected TextView dateView;
    @ViewById(R.id.message)
    protected TextView messageView;
    @ViewById(R.id.participant_name)
    protected TextView participantNameView;
    @ViewById(R.id.participant_image)
    protected ImageView participantImageView;

    @Bean
    protected API api;

    private String conversationId;


    public ConversationElementView(Context context) {
        super(context);
    }

    public ConversationElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(String conversationMetaLink) {
        loadConversationMeta(conversationMetaLink);
    }

    private void loadConversationMeta(String conversationMetaLink) {
        api.firebaseFromLink(conversationMetaLink).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    setConversationMeta(dataSnapshot.getValue(ConversationMetadataDTO.class));
                } else {
                    // error!
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    protected void fetchParticipantData(List<String> participants) {
        for(String participant : participants) {
            // ignore owner
            if(participant.equals(api.getProfile().getId()) == false) {

                api.firebaseForUser(participant).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            addParticipant(dataSnapshot.getValue(ProfileDTO.class));
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        }
    }

    @UiThread
    protected void setConversationMeta(ConversationMetadataDTO conversationMeta) {
        if(conversationMeta != null) {

            conversationId = conversationMeta.getConversationId();

            fetchParticipantData(conversationMeta.getParticipants());

            messageView.setText(
                    String.format(
                            "%s: %s",
                            conversationMeta.getLastSenderName(),
                            conversationMeta.getLastMessageSent())
            );

            if(conversationMeta.getDateLastMessageSent() != null) {
                dateView.setText(dateFormat.format(conversationMeta.getDateLastMessageSent()));
            }

            setOnClickListener(this);
        }
    }

    @UiThread
    protected void addParticipant(ProfileDTO profile) {
        // currently only supporting one profile; whatever you set here overrides the previous value!
        if(profile != null) {
            participantNameView.setText(profile.getName());
            Glide.with(getContext()).load(profile.getAvatarUrlString())
                    .asBitmap()
                    .transform(new CropCircleTransformation(getContext()))
                    .into(participantImageView);
        }
    }

    @Override
    public void onClick(View v) {
        if(conversationId != null) {
            ConversationActivity_.intent(getContext()).conversationId(conversationId).start();
        }
    }
}
