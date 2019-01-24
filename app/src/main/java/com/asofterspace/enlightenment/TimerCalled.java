package com.asofterspace.enlightenment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The timer has been called!
 */
public class TimerCalled extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Toast.makeText(context, "Wakey-wakey! :)", Toast.LENGTH_LONG).show();

        Set<BackendTarget> targets = new HashSet<>();
        targets.add(BackendTarget.BENE);
        // targets.add(BackendTarget.H2);

        BackendThread backendThread = MainActivity.getBackendThread();
        if (backendThread == null) {
            return;
        }
        backendThread.performTask(new BackendTask(targets, "sunrise"));
    }
}
