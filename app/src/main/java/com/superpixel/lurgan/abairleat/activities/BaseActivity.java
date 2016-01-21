package com.superpixel.lurgan.abairleat.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.superpixel.lurgan.abairleat.R;

/**
 * Created by Martin on 1/20/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String getLogTag() {
        return getString(R.string.app_name);
    }

    public void log(int priority, String message) {
        Log.println(priority, getLogTag(), message);
    }

    public void log(int priority, String message, Object... params) {
        Log.println(priority, getLogTag(), String.format(message, params));
    }

    public void toast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }
}
