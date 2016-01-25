package com.superpixel.lurgan.abairleat.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.firebase.client.Query;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;
import com.superpixel.lurgan.abairleat.util.ViewWrapper;
import com.superpixel.lurgan.abairleat.views.MessageElementView;
import com.superpixel.lurgan.abairleat.views.MessageElementView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/25/16.
 */
@EBean
public class MessagesFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<MessageDTO, MessageElementView> {

    @RootContext
    protected Context context;

    @Bean
    protected API api;

    public void setConversationId(String conversationId) {
        Query query = api.firebaseForConversation(conversationId).child("messages").orderByPriority();
        query.keepSynced(true);

        // TODO: 1/25/16 might wanna get rid of the second param ...
        bind(query, MessageDTO.class);
    }

    @Override
    protected MessageElementView onCreateItemView(ViewGroup parent, int viewType) {
        return MessageElementView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MessageElementView> holder, int position) {
        holder.getView().bind(getItem(position));
    }

    @Override
    protected void itemAdded(MessageDTO item, String key, int position) {

    }

    @Override
    protected void itemChanged(MessageDTO oldItem, MessageDTO newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(MessageDTO item, String key, int position) {

    }

    @Override
    protected void itemMoved(MessageDTO item, String key, int oldPosition, int newPosition) {

    }

}
