package com.example.firebase.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.firebase.HomeActivity;

public class BasicJobIntentService extends JobIntentService {
    // id for the service
    private static final int JOB_ID = 12;
    // Aux method to queue the task
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BasicJobIntentService.class, JOB_ID, intent);
    }
    // Method that executes in background
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int milliSeconds = intent.getIntExtra("milliAseconds", 10000);
        try {
            Thread.sleep(milliSeconds);
            Log.i(HomeActivity.TAG, "Service Finished Waiting");
            for (int i = 0; i < 1000; i++) {
                Thread.sleep(1000);
                Log.i(HomeActivity.TAG, "Service finished Waiting "+i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
