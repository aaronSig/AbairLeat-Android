package com.superpixel.lurgan.abairleat.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.views.ContactsView;
import com.superpixel.lurgan.abairleat.views.ContactsView_;
import com.superpixel.lurgan.abairleat.views.ConversationsView;
import com.superpixel.lurgan.abairleat.views.ConversationsView_;
import com.superpixel.lurgan.abairleat.views.DirtyView;
import com.superpixel.lurgan.abairleat.views.ProfileView;
import com.superpixel.lurgan.abairleat.views.ProfileView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by Martin on 1/26/16.
 */
@EBean
public class DashboardPagerAdapter extends PagerAdapter {

    @RootContext
    protected Context context;

    public enum Pages {
        contacts(R.string.contacts, 0),
        conversations(R.string.conversations, 1),
        profile(R.string.profile, 2);

        private int titleResId;
        private int index;

        Pages(int titleResId, int index) {
            this.titleResId = titleResId;
            this.index = index;
        }
    }

    @Override
    public int getCount() {
        return Pages.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (Pages.values()[position]) {
            default:
            case contacts:
                return initContacts(container);

            case conversations:
                return initConversations(container);

            case profile:
                return initProfile(container);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(object instanceof DirtyView) {
            ((DirtyView) object).destroy();
        }

        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(Pages.values()[position].titleResId);
    }

    private View initContacts(ViewGroup container) {
        ContactsView view = ContactsView_.build(context);

        container.addView(view);

        return view;
    }

    private View initConversations(ViewGroup container) {
        ConversationsView view = ConversationsView_.build(context);

        container.addView(view);

        return view;
    }

    private View initProfile(ViewGroup container) {
        ProfileView view = ProfileView_.build(context);

        container.addView(view);

        return view;
    }
}
