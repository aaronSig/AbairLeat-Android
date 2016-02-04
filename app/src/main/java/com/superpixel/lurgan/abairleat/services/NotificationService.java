package com.superpixel.lurgan.abairleat.services;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.dto.ChatNotificationDTO;
import com.superpixel.lurgan.abairleat.dto.StringRealmObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import io.realm.Realm;

/**
 * Created by Martin on 2/3/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class NotificationService {

    private static final String LOG_TAG = "NotificationService";

    @RootContext
    protected Context context;

    private NotificationManager notificationManager;

    @AfterInject
    protected void afterInject() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showChatNotification(ChatNotificationDTO chatNotification) {

        Log.d(LOG_TAG, "Showing notification: " + chatNotification.getConversationId());

        notificationManager.notify(
                chatNotification.getConversationId(),
                chatNotification.NOTIFICATION_ID,
                toNotification(chatNotification, context).build()
        );
    }

    public ChatNotificationDTO generateChatNotification(String title, String message, String conversationId, String userId) {
        Realm realm = Realm.getInstance(context);

        ChatNotificationDTO notification = realm.where(ChatNotificationDTO.class).equalTo("conversationId", conversationId).findFirst();

        realm.beginTransaction();

        if(notification == null) {
            Log.d(LOG_TAG, "Creating new notification for " + conversationId);

            notification = realm.createObject(ChatNotificationDTO.class);

            notification.setTitle(title);
            notification.setLastMessage(message);
            notification.setConversationId(conversationId);
            notification.setUserId(userId);

        } else {
            Log.d(LOG_TAG, "Found existing notification for " + conversationId);

            notification.getMessages().add(new StringRealmObject(message));
            notification.setLastMessage(message);
        }

        realm.commitTransaction();

        return notification;
    }

    public void clearNotification(String conversationLink) {
        Log.d(LOG_TAG, "Clearing notifications for " + conversationLink);

        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        realm.where(ChatNotificationDTO.class).equalTo("conversationId", conversationLink).findAll().clear();
        realm.commitTransaction();

        notificationManager.cancel(conversationLink, ChatNotificationDTO.NOTIFICATION_ID);
    }

    public void clearAll() {
        Log.d(LOG_TAG, "Clearing all notifications");

        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        realm.where(ChatNotificationDTO.class).findAll().clear();
        realm.commitTransaction();

        notificationManager.cancelAll();
    }

    private NotificationCompat.Builder toNotification(ChatNotificationDTO notification, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // TODO: 2/3/16 add large icon with user pic
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getLastMessage());

        if(notification.getMessages().size() > 1) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(notification.getTitle());
            inboxStyle.setSummaryText("Abair Leat");

            for(int i=0; i<notification.getMessages().size(); i++) {
                inboxStyle.addLine(notification.getMessages().get(i).getValue());
            }

            builder.setStyle(inboxStyle);
        }

        return builder;
    }
}
