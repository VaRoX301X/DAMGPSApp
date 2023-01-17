package com.dam.gpsapp.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class pruebaIntentService extends IntentService {

    int currentState=0;

    public pruebaIntentService() {
        super("IntentServicePruebaWorker");  // super the name of worker thread, it is necessary.
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++currentState;
            Log.d("db", "currentState : " + currentState);
        }  // End of while
    }
}