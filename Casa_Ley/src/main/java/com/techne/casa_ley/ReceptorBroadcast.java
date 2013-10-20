package com.techne.casa_ley;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Victor on 20/10/13.
 */
public class ReceptorBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, Servicio.class);
        context.startService(service);
    }
}
