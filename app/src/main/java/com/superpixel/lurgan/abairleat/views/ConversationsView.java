package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.ConversationsAdapter;
import com.superpixel.lurgan.abairleat.util.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/26/16.
 */
@EViewGroup(R.layout.view_conversations)
public class ConversationsView extends RelativeLayout implements DirtyView {

    @Bean
    protected ConversationsAdapter conversationsAdapter;

    @ViewById(R.id.conversations)
    protected RecyclerView conversationsRecyclerView;

    public ConversationsView(Context context) {
        super(context);
    }

    public ConversationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void afterViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        conversationsRecyclerView.setLayoutManager(layoutManager);
        conversationsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        conversationsRecyclerView.setAdapter(conversationsAdapter);
    }

    @Override
    public void destroy() {
        conversationsAdapter.destroy();
    }
}
