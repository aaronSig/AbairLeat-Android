package com.superpixel.lurgan.abairleat.dto;

import com.superpixel.lurgan.abairleat.api.API;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Martin on 1/25/16.
 */
public class ConversationMetadataDTO extends FirebaseDTO {

    private String conversationId;
    private boolean oneOnOne;

    private Date dateLastMessageSent;

    private String lastMessageSent;
    private String lastSenderAvatarUrl;
    private String lastSenderName;

    private List<String> participants;

    public ConversationMetadataDTO() {}

    public ConversationMetadataDTO(String conversationId, boolean oneOnOne, Date dateLastMessageSent,
                                   String lastMessageSent, String lastSenderAvatarUrl,
                                   String lastSenderName, List<String> participants) {

        this.conversationId = conversationId;
        this.oneOnOne = oneOnOne;

        this.dateLastMessageSent = dateLastMessageSent;

        this.lastMessageSent = lastMessageSent;
        this.lastSenderAvatarUrl = lastSenderAvatarUrl;
        this.lastSenderName = lastSenderName;

        this.participants = participants;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isOneOnOne() {
        return oneOnOne;
    }

    public void setOneOnOne(boolean oneOnOne) {
        this.oneOnOne = oneOnOne;
    }

    public Date getDateLastMessageSent() {
        return dateLastMessageSent;
    }

    public void setDateLastMessageSent(Date dateLastMessageSent) {
        this.dateLastMessageSent = dateLastMessageSent;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public String getLastSenderAvatarUrl() {
        return lastSenderAvatarUrl;
    }

    public void setLastSenderAvatarUrl(String lastSenderAvatarUrl) {
        this.lastSenderAvatarUrl = lastSenderAvatarUrl;
    }

    public String getLastSenderName() {
        return lastSenderName;
    }

    public void setLastSenderName(String lastSenderName) {
        this.lastSenderName = lastSenderName;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "ConversationMetadataDTO{" +
                "conversationId='" + conversationId + '\'' +
                ", oneOnOne=" + oneOnOne +
                ", dateLastMessageSent=" + dateLastMessageSent +
                ", lastMessageSent='" + lastMessageSent + '\'' +
                ", lastSenderAvatarUrl='" + lastSenderAvatarUrl + '\'' +
                ", lastSenderName='" + lastSenderName + '\'' +
                ", participants=" + participants +
                '}';
    }

    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("conversationId", getConversationId());
        map.put("oneOnOne", isOneOnOne());
        map.put("dateLastMessageSent", formatDate(getDateLastMessageSent()));
        map.put("lastMessageSent", getLastMessageSent());
        map.put("lastSenderAvatarUrl", getLastSenderAvatarUrl());
        map.put("lastSenderName", getLastSenderName());
        map.put("participants", getParticipants());

        return map;
    }

    public HashMap<String, Object> generateProfileMetaLinkMap() {
        HashMap<String, Object> map = new HashMap<>(1);

        map.put(API.linkify(this), true);

        return map;
    }
}
