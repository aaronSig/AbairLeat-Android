package com.superpixel.lurgan.abairleat.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.MessagesFirebaseRecyclerAdapter;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ConversationMetadataDTO;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;
import com.superpixel.lurgan.abairleat.services.NotificationService;
import com.superpixel.lurgan.abairleat.util.ProfileCache;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Martin on 1/25/16.
 */
@EActivity(R.layout.activity_conversation)
public class ConversationActivity extends BaseActivity {

    @Extra
    protected String conversationId;

    @Bean
    protected API api;
    @Bean
    protected MessagesFirebaseRecyclerAdapter messagesAdapter;
    @Bean
    protected NotificationService notificationService;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbarView;
    @ViewById(R.id.title)
    protected TextView titleView;
    @ViewById(R.id.edit)
    protected EditText editTextView;
    @ViewById(R.id.send)
    protected Button sendButtonView;
    @ViewById(R.id.messages)
    protected RecyclerView messagesRecyclerView;

    private Firebase conversationFirebaseRef;
    private ConversationMetadataDTO metadataDTO;

    private String title;



    @Override
    protected void onResume() {
        super.onResume();

        if(conversationId != null) {
            notificationService.clearNotification(conversationId);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        messagesAdapter.destroy();
    }

    @AfterExtras
    protected void afterExtras() {
        conversationFirebaseRef = api.firebaseForConversation(conversationId);

        notificationService.clearNotification(conversationId);

        setInitialConversationMeta();
    }

    @AfterViews
    protected void afterViews() {

        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        messagesAdapter.initialize(conversationId);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);

        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messagesAdapter);

        setTitle(title);
    }

    private void setInitialConversationMeta() {

        conversationFirebaseRef.child("metadata").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    metadataDTO = dataSnapshot.getValue(ConversationMetadataDTO.class);
                } else {
                    metadataDTO = new ConversationMetadataDTO();
                    updateConversationMeta(metadataDTO);
                }

                title = generateTitle(metadataDTO);
                setTitle(title);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                log(Log.ERROR, firebaseError.toString());
            }
        });
    }

    private void updateConversationMeta(ConversationMetadataDTO meta) {
        String[] participantsArr = conversationId.replace("1x", "").split("-");

        List<String> participants = new ArrayList<>(2);

        participants.add(participantsArr[0]);
        participants.add(participantsArr[1]);

        meta.setConversationId(conversationId);
        meta.setOneOnOne(true);
        meta.setParticipants(participants);

        conversationFirebaseRef.child("metadata").updateChildren(meta.toMap());
    }

    private String generateTitle(ConversationMetadataDTO meta) {
        List<String> participants = new ArrayList<>();

        for(String participant : meta.getParticipants()) {
            if(participant.equals(api.getProfile().getId()) == false) {
                participants.add(ProfileCache.get(participant).getFirstName());
            }
        }

        return TextUtils.join(",", participants);
    }

    private void sendMessage(String message) {

        final Date now = new Date(System.currentTimeMillis());

        // create and send the message
        final MessageDTO messageDto = new MessageDTO(api.getProfile().getId(), now, message);

        updateSendingStatus(true, false);

        Firebase messageRef = conversationFirebaseRef.child("messages").push();
        messageRef.setValue(
                messageDto.toMap(),
//                String.valueOf(API.getCurrentGmtMillis()),

                new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if(firebaseError == null) {
                            afterMessageSent(messageDto, now);
                            updateSendingStatus(false, true);
                        } else {
                            Snackbar.make(editTextView, firebaseError.toString(), Snackbar.LENGTH_LONG).show();
                            updateSendingStatus(false, false);
                        }

                    }
                }
        );
    }

    private void afterMessageSent(MessageDTO message, Date dateSent) {
        // update meta

        metadataDTO.setLastMessageSent(message.getText());
        metadataDTO.setLastSenderName(api.getProfile().getName());
        metadataDTO.setLastSenderAvatarUrl(api.getProfile().getAvatarUrlString());
        metadataDTO.setDateLastMessageSent(dateSent);

        updateConversationMeta(metadataDTO);

        // add meta link to participating user profiles

        for(String participantId : metadataDTO.getParticipants()) {
            api.firebaseForUser(participantId)
                    .child("conversations-metadata")
                    .child(API.linkify(metadataDTO))
                    .setValue(true, 0L-API.getCurrentGmtMillis());
        }
    }

    @UiThread
    protected void setTitle(String title) {
        if(title != null) {
            titleView.setText(title);
        }
    }

    @UiThread
    protected void updateSendingStatus(boolean sending, boolean clearText) {
        if(clearText) {
            editTextView.setText("");
        }

        if(sending) {
            sendButtonView.setEnabled(false);
            editTextView.setEnabled(false);
        } else {
            sendButtonView.setEnabled(true);
            editTextView.setEnabled(true);
        }
    }


    @TextChange(R.id.edit)
    protected void validateMessage() {
        // TODO: 1/25/16 validation
    }


    @Click(R.id.send)
    protected void onSendButtonClicked() {
        // validation
        String text = editTextView.getText().toString();
        sendMessage(text);
//        editTextView.setText("");
    }



}
