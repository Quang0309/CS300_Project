package com.example.admin.testfirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Bunny on 11/17/2017.
 */

public class RingtonePlayService extends Service{
    MediaPlayer mediaSong;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("Local Service", "Receive start id " + startId + ": " + intent);

        // Get the extra string value
        String state = intent.getExtras().getString("extra");

        // Convert extra string from the intent to startId (0 or 1)
        assert state != null;
        switch (state) {
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        // No ringtone + on => set ringtone
        if (!this.isRunning && startId == 1) {
            playRingtone();
            notificationPopUp();
        }
        // Ringtone + off => turn off alarm
        if (this.isRunning && startId == 0) {
            stopRingtone();
        }
        // Ringtone + off => nothing happen
        if (!this.isRunning && startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }
        // Ringtone + on => continue ringtone
        if (this.isRunning && startId == 1) {
            playRingtone();
            notificationPopUp();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        this.isRunning = false;
    }

    private void notificationPopUp() {
        Intent mainActivityIntent = new Intent(this.getApplicationContext(), RoomDetail.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("FOOTBALL TIME");
        notification.setContentText("Your football match will start soon");
        notification.setContentIntent(pendingIntent);

        final int notifyId = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notification.build());
    }

    private void playRingtone() {
        mediaSong = MediaPlayer.create(this, R.raw.zoops1);
        mediaSong.start();

        this.isRunning = true;
        this.startId = 1;
    }

    private void stopRingtone() {
        mediaSong.stop();
        mediaSong.reset();

        this.isRunning = false;
        this.startId = 0;
    }
}

