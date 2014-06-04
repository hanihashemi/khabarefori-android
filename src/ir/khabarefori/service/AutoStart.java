package ir.khabarefori.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hani on 1/21/14.
 */
public class AutoStart extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context,CheckServerService.class);
        context.startService(intentService);
    }
}
