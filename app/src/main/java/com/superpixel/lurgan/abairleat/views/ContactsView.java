package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.ContactsFirebaseRecyclerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/26/16.
 */
@EViewGroup(R.layout.view_contacts)
public class ContactsView extends RelativeLayout implements DirtyView {

    @Bean
    protected ContactsFirebaseRecyclerAdapter contactsAdapter;

    @ViewById(R.id.contacts)
    protected RecyclerView contactsRecyclerView;

    public ContactsView(Context context) {
        super(context);
    }

    public ContactsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void afterViews() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        contactsRecyclerView.setLayoutManager(layoutManager);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }

    @Override
    public void destroy() {
        contactsAdapter.destroy();
    }
}
