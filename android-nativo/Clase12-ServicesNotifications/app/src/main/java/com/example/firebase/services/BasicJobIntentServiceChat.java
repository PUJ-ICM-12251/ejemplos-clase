package com.example.firebase.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.firebase.ChatActivity;
import com.example.firebase.HomeActivity;
import com.example.firebase.R;
import com.example.firebase.adapters.MessageAdapter;
import com.example.firebase.model.DatabasePaths;
import com.example.firebase.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BasicJobIntentServiceChat extends JobIntentService {
    // id for the service
    private static final int JOB_ID = 12;

    public static final String CHANNEL_ID = "MyApp";
    public static final int notificationId = 10;

    // Variables for Firebase DB
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener valueEventListener;

    // Local Data
    boolean firstLoad;

    // Aux method to queue the task
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BasicJobIntentServiceChat.class, JOB_ID, intent);
    }
    // Method that executes in background
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        createNotificationChannel();
        loadMessagesNotifications();
    }


    public void loadMessagesNotifications() {
        firstLoad = true;
        myRef = database.getReference(DatabasePaths.CHAT);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!firstLoad) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Notification builder
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID);
                        mBuilder.setSmallIcon(R.drawable.chat_icon);
                        mBuilder.setContentTitle("New Message");
                        mBuilder.setContentText("You have a new message in the chat activity.");

                        // Action associated to notification
                        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true); // Removes notification when touching it

                        // Throw notification
                        int notificationId = 001;
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                        // NotificationId es un entero único definido para cada notificatión que se lanza
                        notificationManager.notify(notificationId, mBuilder.build());
                    }
                } else {
                    firstLoad = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Background", "Error in query", databaseError.toException());
            }
        });
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // IMPORTANCE MAX MUESTRA LA NOTIFICACIÓN ANIMADA
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can`t change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}