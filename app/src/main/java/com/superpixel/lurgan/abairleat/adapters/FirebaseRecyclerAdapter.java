package com.superpixel.lurgan.abairleat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.annotations.Nullable;
import com.superpixel.lurgan.abairleat.util.ViewWrapper;

import java.util.ArrayList;

/**
 * Created by Martin on 1/25/16.
 *
 * Based on:
 * https://github.com/mmazzarolo/firebase-recyclerview/blob/master/app/src/main/java/com/example/matteo/firebase_recycleview/FirebaseRecyclerAdapter.java
 *
 */
public abstract class FirebaseRecyclerAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    private Query query;
    private Class<T> elementClass;
    private ArrayList<T> elements;
    private ArrayList<String> keys;

    /**
     * @param query     The Firebase location to watch for data changes.
     *                  Can also be a slice of a location, using some combination of
     *                  <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>.
     * @param itemClass The class of the items.
     */
    public void bind(Query query, Class<T> itemClass) {
        bind(query, itemClass, null, null);
    }

    /**
     * @param query     The Firebase location to watch for data changes.
     *                  Can also be a slice of a location, using some combination of
     *                  <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>.
     * @param itemClass The class of the items.
     * @param items     List of items that will load the adapter before starting the listener.
     *                  Generally null or empty, but this can be useful when dealing with a
     *                  configuration change (e.g.: reloading the adapter after a device rotation).
     *                  Be careful: keys must be coherent with this list.
     * @param keys      List of keys of items that will load the adapter before starting the listener.
     *                  Generally null or empty, but this can be useful when dealing with a
     *                  configuration change (e.g.: reloading the adapter after a device rotation).
     *                  Be careful: items must be coherent with this list.
     */
    public void bind(Query query, Class<T> itemClass, @Nullable ArrayList<T> items, @Nullable ArrayList<String> keys) {
        this.query = query;
        if (items != null && keys != null) {
            this.elements = items;
            this.keys = keys;
        } else {
            elements = new ArrayList<T>();
            this.keys = new ArrayList<String>();
        }
        this.elementClass = itemClass;
        query.addChildEventListener(eventListener);
    }

    private ChildEventListener eventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();

            if (!keys.contains(key)) {
                T item = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.elementClass);
                int insertedPosition;
                if (previousChildName == null) {
                    elements.add(0, item);
                    keys.add(0, key);
                    insertedPosition = 0;
                } else {
                    int previousIndex = keys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == elements.size()) {
                        elements.add(item);
                        keys.add(key);
                    } else {
                        elements.add(nextIndex, item);
                        keys.add(nextIndex, key);
                    }
                    insertedPosition = nextIndex;
                }
                notifyItemInserted(insertedPosition);
                itemAdded(item, key, insertedPosition);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();

            if (keys.contains(key)) {
                int index = keys.indexOf(key);
                T oldItem = elements.get(index);
                T newItem = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.elementClass);

                elements.set(index, newItem);

                notifyItemChanged(index);
                itemChanged(oldItem, newItem, key, index);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            if (keys.contains(key)) {
                int index = keys.indexOf(key);
                T item = elements.get(index);

                keys.remove(index);
                elements.remove(index);

                notifyItemRemoved(index);
                itemRemoved(item, key, index);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();

            int index = keys.indexOf(key);
            T item = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.elementClass);
            elements.remove(index);
            keys.remove(index);
            int newPosition;
            if (previousChildName == null) {
                elements.add(0, item);
                keys.add(0, key);
                newPosition = 0;
            } else {
                int previousIndex = keys.indexOf(previousChildName);
                int nextIndex = previousIndex + 1;
                if (nextIndex == elements.size()) {
                    elements.add(item);
                    keys.add(key);
                } else {
                    elements.add(nextIndex, item);
                    keys.add(nextIndex, key);
                }
                newPosition = nextIndex;
            }
            notifyItemMoved(index, newPosition);
            itemMoved(item, key, index, newPosition);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur.");
        }

    };

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return (elements != null) ? elements.size() : 0;
    }

    /**
     * Clean the adapter.
     * ALWAYS call this method before destroying the adapter to remove the listener.
     */
    public void destroy() {
        query.removeEventListener(eventListener);
    }

    /**
     * Returns the list of items of the adapter: can be useful when dealing with a configuration
     * change (e.g.: a device rotation).
     * Just save this list before destroying the adapter and pass it to the new adapter (in the
     * constructor).
     *
     * @return the list of items of the adapter
     */
    public ArrayList<T> getItems() {
        return elements;
    }

    /**
     * Returns the list of keys of the items of the adapter: can be useful when dealing with a
     * configuration change (e.g.: a device rotation).
     * Just save this list before destroying the adapter and pass it to the new adapter (in the
     * constructor).
     *
     * @return the list of keys of the items of the adapter
     */
    public ArrayList<String> getKeys() {
        return keys;
    }

    public String getKey(int position) {
        return keys.get(position);
    }

    /**
     * Returns the item in the specified position
     *
     * @param position Position of the item in the adapter
     * @return the item
     */
    public T getItem(int position) {
        return elements.get(position);
    }

    /**
     * Returns the position of the item in the adapter
     *
     * @param item Item to be searched
     * @return the position in the adapter if found, -1 otherwise
     */
    public int getPositionForItem(T item) {
        return elements != null && elements.size() > 0 ? elements.indexOf(item) : -1;
    }

    /**
     * Check if the searched item is in the adapter
     *
     * @param item Item to be searched
     * @return true if the item is in the adapter, false otherwise
     */
    public boolean contains(T item) {
        return elements != null && elements.contains(item);
    }

    /**
     * ABSTRACT METHODS THAT MUST BE IMPLEMENTED BY THE EXTENDING ADAPTER.
     */

    /**
     * Called after an item has been added to the adapter
     *
     * @param item     Added item
     * @param key      Key of the added item
     * @param position Position of the added item in the adapter
     */
    protected abstract void itemAdded(T item, String key, int position);

    /**
     * Called after an item changed
     *
     * @param oldItem  Old version of the changed item
     * @param newItem  Current version of the changed item
     * @param key      Key of the changed item
     * @param position Position of the changed item in the adapter
     */
    protected abstract void itemChanged(T oldItem, T newItem, String key, int position);

    /**
     * Called after an item has been removed from the adapter
     *
     * @param item     Removed item
     * @param key      Key of the removed item
     * @param position Position of the removed item in the adapter
     */
    protected abstract void itemRemoved(T item, String key, int position);

    /**
     * Called after an item changed position
     *
     * @param item        Moved item
     * @param key         Key of the moved item
     * @param oldPosition Old position of the changed item in the adapter
     * @param newPosition New position of the changed item in the adapter
     */
    protected abstract void itemMoved(T item, String key, int oldPosition, int newPosition);

}
