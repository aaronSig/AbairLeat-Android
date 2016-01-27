package com.superpixel.lurgan.abairleat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.superpixel.lurgan.abairleat.R;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by Martin on 1/26/16.
 */
@EViewGroup(R.layout.view_conversations)
public class ConversationsView extends RelativeLayout {

    public ConversationsView(Context context) {
        super(context);
    }

    public ConversationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
