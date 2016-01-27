package com.superpixel.lurgan.abairleat.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.firebase.client.Query;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.MessageDTO;
import com.superpixel.lurgan.abairleat.util.ViewWrapper;
import com.superpixel.lurgan.abairleat.views.MessageView;
import com.superpixel.lurgan.abairleat.views.OwnerMessageView_;
import com.superpixel.lurgan.abairleat.views.ParticipantMessageView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/25/16.
 */
@EBean
public class MessagesFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<MessageDTO, MessageView> {

    public static final int VIEW_TYPE_MESSAGE_OWNER = 0;
    public static final int VIEW_TYPE_MESSAGE_PARTICIPANT = 1;

    @RootContext
    protected Context context;

    @Bean
    protected API api;

    private Query query;

    public void initialize(String conversationId) {
        query = api.firebaseForConversation(conversationId).child("messages").orderByPriority();
        query.keepSynced(true);

        // TODO: 1/25/16 might wanna get rid of the second param ...
        bind(query, MessageDTO.class);
    }

    @Override
    protected MessageView onCreateItemView(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case VIEW_TYPE_MESSAGE_OWNER:
                return OwnerMessageView_.build(context);

            case VIEW_TYPE_MESSAGE_PARTICIPANT:
                return ParticipantMessageView_.build(context);

        }
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MessageView> holder, int position) {
        holder.getView().bind(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {

        if(getItem(position).getAuthor().equals(api.getProfile().getId())) {
            return VIEW_TYPE_MESSAGE_OWNER;
        }

        return VIEW_TYPE_MESSAGE_PARTICIPANT;
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
