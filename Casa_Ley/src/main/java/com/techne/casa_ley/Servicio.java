package com.techne.casa_ley;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Victor on 20/10/13.
 */
public class Servicio extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static SoapObject TIENDAS;
    private static long tiempo = 0;
    private static long tiempoLast = 0;
    private static float corta = Float.MAX_VALUE;
    private static GPSTracker gps;

    @Override
    public void onCreate() {
        super.onCreate();
        gps = new GPSTracker(Servicio.this);
    }

    @Override
    public void onDestroy() {
        gps.stopUsingGPS();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Config c = null;
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

        try{
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            c = db.getConfig();
            db.closeDB();
            if(c.getLocal() == 1 && gps.canGetLocation())
            {
                if(TIENDAS == null)
                    TIENDAS = obtenerTiendas();
                tiempoLast = System.currentTimeMillis()/1000;
                if(TIENDAS != null && tiempo <= tiempoLast)
                {
                    for(int i = 0; i < TIENDAS.getPropertyCount(); i++)
                    {
                        SoapObject ti = (SoapObject)TIENDAS.getProperty(i);
                        if(ti.getPropertyCount() > 0)
                        {
                            float []results = new float[1];
                            double lat = gps.getLatitude();
                            double longi = gps.getLongitude();
                            Location.distanceBetween(Double.parseDouble(ti.getProperty(4).toString()), Double.parseDouble(ti.getProperty(3).toString()),
                                    lat, longi, results);

                            if(c.getTiendas() >= results[0])
                            {
                                if(corta > results[0] || tiempo <= tiempoLast)
                                {
                                    tiempo = tiempoLast + (((10 * 60) + 59) * 1000);
                                    corta = results[0];
                                    notificarTienda(ti.getProperty(7).toString(), results[0]);
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){}

        return Service.START_STICKY;
    }

    private void notificarTienda(String nombre, float distancia){
        Intent resultIntent = new Intent(this, TiendaInfoActivity.class);
        resultIntent.putExtra("nombre", nombre);
        resultIntent.putExtra("distancia", distancia + "");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(nombre)
                        .setContentText("Distancia: " + distancia)
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

        notificationManager.notify(1, mBuilder.build());
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

    private SoapObject obtenerTiendas()
    {
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("cve_estado", "");
        params.put("nombre_ciudad", "");
        return Util.sendSOAPwithData("GetStores", params);
    }

}
