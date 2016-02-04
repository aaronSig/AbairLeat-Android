package com.superpixel.lurgan.abairleat.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.superpixel.lurgan.abairleat.dto.ChatNotificationDTO;
import com.superpixel.lurgan.abairleat.services.NotificationService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

/**
 * Created by Martin on 2/2/16.
 */
@EService
public class AbairGcmListenerService extends GcmListenerService {
    private static final String LOG_TAG = "AbairGcmListenerService";

    @Bean
    protected NotificationService notificationService;

    public AbairGcmListenerService() {
        super();
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        Log.e(LOG_TAG, "message received!");
        Log.e(LOG_TAG, "# from:    " + from);

        Log.e(LOG_TAG, "# id:      " + data.getString("conversationId"));
        Log.e(LOG_TAG, "# userId:  " + data.getString("userId"));
        Log.e(LOG_TAG, "# title:   " + data.getString("title"));
        Log.e(LOG_TAG, "# message: " + data.getString("message"));
        Log.e(LOG_TAG, "# type:    " + data.getString("notificationType"));

        ChatNotificationDTO notification = notificationService.generateChatNotification(
                data.getString("title"),
                data.getString("message"),
                data.getString("conversationId"),
                data.getString("userId")
        );

        notificationService.showChatNotification(notification);
    }
}
