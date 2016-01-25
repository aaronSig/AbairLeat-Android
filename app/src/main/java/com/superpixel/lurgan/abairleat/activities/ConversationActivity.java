package com.superpixel.lurgan.abairleat.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.MessagesFirebaseRecyclerAdapter;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ConversationMetadataDTO;
import com.superpixel.lurgan.abairleat.dto.FirebaseDTO;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ViewById(R.id.conversation_id)
    protected TextView conversationIdView;
    @ViewById(R.id.edit)
    protected EditText editTextView;
    @ViewById(R.id.messages)
    protected RecyclerView messagesRecyclerView;

    private Firebase conversationFirebaseRef;

    private ConversationMetadataDTO metadataDTO;

    @AfterViews
    protected void afterViews() {
        conversationIdView.setText(conversationId);

        messagesAdapter.setConversationId(conversationId);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messagesAdapter);
    }

    @AfterExtras
    protected void afterExtras() {
        conversationFirebaseRef = api.firebaseForConversation(conversationId);

        metadataDTO = new ConversationMetadataDTO();

        updateConversationMeta(metadataDTO);
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

    private void sendMessage(String message) {

        Date now = new Date(System.currentTimeMillis());

        // create and send the message

        MessageDTO messageDto = new MessageDTO(api.getProfile().getId(), now, message);

        Firebase messageRef = conversationFirebaseRef.child("messages").push();

        messageRef.setValue(
                messageDto.toMap(),
                String.valueOf(now.getTime()),

                new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // update meta

        metadataDTO.setLastMessageSent(message);
        metadataDTO.setLastSenderName(api.getProfile().getName());
        metadataDTO.setLastSenderAvatarUrl(api.getProfile().getAvatarUrlString());
        metadataDTO.setDateLastMessageSent(now);

        updateConversationMeta(metadataDTO);

        // add meta link to participating user profiles

        for(String participantId : metadataDTO.getParticipants()) {
            api.firebaseForUser(participantId)
                    .child("conversation-metadata")
                    .setValue(metadataDTO.generateProfileMetaLinkMap());
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
        editTextView.setText("");
    }



}
