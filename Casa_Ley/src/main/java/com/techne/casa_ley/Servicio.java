package com.techne.casa_ley;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Victor on 20/10/13.
 */
public class Servicio extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try{
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            List<Recordatorio> r = db.getAllRecordatorios();
            db.closeDB();
            Calendar cal = Calendar.getInstance();
            Calendar actual = Calendar.getInstance();
            actual.add(Calendar.MONTH, 1);
            for(Recordatorio rr : r)
            {
                cal.setTimeInMillis(rr.getFecha());
                if(actual.getTimeInMillis() >= cal.getTimeInMillis())
                {
                    db = new DatabaseHelper(getApplicationContext());
                    db.deleteRecordatorio(rr.get_id());
                    db.closeDB();
                    notificar(rr.getTitulo());
                }
            }
        }catch(Exception e){}

        return Service.START_STICKY;
    }

    private void notificar(String rec)
    {
        Intent resultIntent = new Intent(this, MainActivity.class);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(rec)
                        .setContentText("Recordatorio Ley")
                        .setSound(soundUri)
                        .setAutoCancel(true);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }
}
