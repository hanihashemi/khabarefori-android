package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import ir.khabarefori.json.JsonGetNewNews;

import java.util.*;

/**
 * Created by hani on 1/19/14.
 */
public class ServiceCheckServer extends Service {
    private final String LOGTAG = "ServiceCheckServer";
    private static Timer timer;
    private Context context;

    private List<Messenger> clients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    private final Messenger messenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to send messages to IncomingHandler.

    public class MessageType {
        public static final int MSG_1 = 0;
        public static final int MSG_REGISTER_CLIENT = 1;
        public static final int MSG_UNREGISTER_CLIENT = 2;
        public static final int MSG_2 = 3;
    }

    private int serial_num = 0;

    /**
     * @param arg0
     * @return
     */
    public IBinder onBind(Intent arg0) {
        Log.d(LOGTAG, "onBind");
        return messenger.getBinder();
    }


    /**
     * this method call when service create , not when start
     */
    public void onCreate() {
        super.onCreate();

        Log.d("Service", "_____________________Service Started_________________");

        context = this;

        Random randomGenerator = new Random();
        serial_num = randomGenerator.nextInt(1000);

        Log.d("service", "onCreate" + serial_num);

        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 5000, 600000);
            Log.d("service", "Timer start " + serial_num);
        } else {
            Log.d("service", "Timer Set null " + serial_num);
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 5000, 600000);
        }
    }

//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("service", "onStartCommand");
//        if (timer.)
//        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
//        return android.app.Service.START_STICKY;
//    }

    /**
     * Do some work in thread , by default service run in main thread :O
     */
    private class mainTask extends TimerTask {
        public void run() {
//            Log.d(LOGTAG, "timer " + serial_num);
//            sendMessageToUI();
            Log.d(LOGTAG, "timer " + serial_num);

            JsonGetNewNews.CheckNews();
        }
    }

    /**
     * this method call when service kill
     */
    @Override
    public void onDestroy() {
        Log.d(LOGTAG, "_______________Destroy Service Stop_________________");
        restartService();

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(LOGTAG, "_______________Task Service Stop_________________");
        restartService();
    }

    private void restartService() {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                restartServicePendingIntent);
    }


    /**
     * Handle toast message
     */
    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNotification() {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // In this sample, we'll use the same text for the ticker and the expanded notification
//        CharSequence text = getText(R.string.service_started);
//        // Set the icon, scrolling text and timestamp
//        android.app.Notification notification = new android.app.Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
//        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(this, getText(R.string.service_label), text, contentIntent);
//        // Send the notification.
//        // We use a layout id because it is a unique number.  We use it later to cancel.
//        mNotificationManager.notify(R.string.service_started, notification);
    }

    /**
     * Send the data to all clients.
     */
    private void sendMessageToUI() {
        Iterator<Messenger> messengerIterator = clients.iterator();
        while (messengerIterator.hasNext()) {
            Messenger messenger = messengerIterator.next();
            try {
                // Send data as an Integer
//                messenger.send(Message.obtain(null, MSG_SET_INT_VALUE, intvaluetosend, 0));

                // Send data as a String
                Bundle bundle = new Bundle();
                bundle.putString("str1", "abcd");
                Message msg = Message.obtain(null, MessageType.MSG_2);
                msg.setData(bundle);
                messenger.send(msg);

            } catch (RemoteException e) {
                // The client is dead. Remove it from the list.
                clients.remove(messenger);
            }
        }
    }

    /**
     * Handle incoming messages from MainActivity
     */
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(LOGTAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case MessageType.MSG_REGISTER_CLIENT:
                    clients.add(msg.replyTo);
                    break;
                case MessageType.MSG_UNREGISTER_CLIENT:
                    clients.remove(msg.replyTo);
                    break;
                case MessageType.MSG_1:
                    Log.d(LOGTAG, "received message");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}

