package com.superpixel.lurgan.abairleat.dto;

import java.util.HashMap;

/**
 * Created by Martin on 1/25/16.
 */
public class ConversationDTO extends FirebaseDTO {
    private HashMap<String, MessageDTO> messages;
    private ConversationMetadataDTO metadata;

    public ConversationDTO(HashMap<String, MessageDTO> messages, ConversationMetadataDTO metadata) {
        this.messages = messages;
        this.metadata = metadata;
    }

    public HashMap<String, MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, MessageDTO> messages) {
        this.messages = messages;
    }

    public ConversationMetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(ConversationMetadataDTO metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ConversationDTO{" +
                "messages=" + messages +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("messages", getMessages());
        map.put("metadata", getMetadata());

        return map;
    }
}
