package com.superpixel.lurgan.abairleat.dto;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Martin on 2/3/16.
 */
public class ChatNotificationRealm extends RealmObject {

    public static final int NOTIFICATION_ID = 0x10100001;

    private String userId;
    private String title;
    private String lastMessage;
    private String conversationId;

    private RealmList<StringRealmObject> messages = new RealmList<>();

    public ChatNotificationRealm() {}

    public ChatNotificationRealm(String title, String lastMessage, String conversationId, String userId) {
        this.userId = userId;
        this.title = title;
        this.lastMessage = lastMessage;
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public RealmList<StringRealmObject> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<StringRealmObject> messages) {
        this.messages = messages;
    }
}
