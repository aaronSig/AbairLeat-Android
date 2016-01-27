package com.superpixel.lurgan.abairleat.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.dto.ProfileDTO;
import com.superpixel.lurgan.abairleat.util.ProfileCache;
import com.superpixel.lurgan.abairleat.util.ViewWrapper;
import com.superpixel.lurgan.abairleat.views.ContactsElementView;
import com.superpixel.lurgan.abairleat.views.ContactsElementView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/25/16.
 */
@EBean
public class ContactsFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<String, ContactsElementView> {

    @RootContext
    protected Context context;

    @Bean
    protected API api;

    @AfterInject
    protected void init() {
        Query query = api.firebaseForCurrentUser().child("contacts").orderByPriority();
        query.keepSynced(true);

        // TODO: 1/25/16 might wanna get rid of the second param ...
        bind(query, String.class);
    }

    @Override
    protected ContactsElementView onCreateItemView(ViewGroup parent, int viewType) {
        return ContactsElementView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ContactsElementView> holder, int position) {
        holder.getView().bind(getItem(position), getKey(position));

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
