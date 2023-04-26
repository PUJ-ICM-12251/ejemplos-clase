package com.example.firebase.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class BasicIntentService extends IntentService {
    private static final String TAG = BasicIntentService.class.getName();

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param "name" Used to name the worker thread, important only for debugging.
     * @deprecated
     */
    public BasicIntentService() {
        super("BasicIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Trabajo que debe hacer el servicio
        // Por ahora solo espera 5 segundos
        try {
            Thread.sleep(5000);
            Log.i(TAG, "Servicio en ejecución");
            for (int i=0; i<100; i++) {
                Thread.sleep(1000);
                Log.i(TAG, "Servicio i: "+i);
            }
        } catch (InterruptedException e) {
            // Restore interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
