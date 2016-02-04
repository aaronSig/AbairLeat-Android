package com.superpixel.lurgan.abairleat.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.firebase.client.Query;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.util.ViewWrapper;
import com.superpixel.lurgan.abairleat.views.ConversationElementView;
import com.superpixel.lurgan.abairleat.views.ConversationElementView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/28/16.
 */
@EBean
public class ConversationsAdapter extends FirebaseRecyclerAdapter<String, ConversationElementView> {

    @RootContext
    protected Context context;

    @Bean
    protected API api;

    @AfterInject
    protected void init() {
        Query query = api.firebaseForCurrentUser().child("conversations-metadata").orderByPriority();
        query.keepSynced(true);

        bind(query, String.class);
    }

    @Override
    protected ConversationElementView onCreateItemView(ViewGroup parent, int viewType) {
        return ConversationElementView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ConversationElementView> holder, int position) {
        holder.getView().bind(getKey(position));
    }

    @Override
    protected void itemAdded(String item, String key, int position) {

    }

    @Override
    protected void itemChanged(String oldItem, String newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(String item, String key, int position) {

    }

    @Override
    protected void itemMoved(String item, String key, int oldPosition, int newPosition) {

    }
}
