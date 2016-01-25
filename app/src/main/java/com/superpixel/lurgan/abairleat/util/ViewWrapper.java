package com.superpixel.lurgan.abairleat.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Martin on 1/25/16.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}
