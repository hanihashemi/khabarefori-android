package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.notify.Knotify;
import ir.khabarefori.service.ServiceCheckServer;

import java.util.ArrayList;

public class MyActivity extends Activity implements View.OnClickListener, ServiceConnection {
    private final String LOGTAG = "MyActivity";
    private final Messenger messenger = new Messenger(new IncomingMessageHandler());
    boolean isBound = false;
    private ServiceCheckServer serviceCheck;
    private Messenger serviceMessenger = null;
    private ServiceConnection serviceConnection = this;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(this, generateData(), listView);
        listView.setAdapter(adapter);

        ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(this);

        Knotify.updateMainActivity(this);

        // bind service :P
        doBindService();

        // run service
        if (!isSCheckServerRunning())
            startService(new Intent(this, ServiceCheckServer.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Knotify.updateMainActivity(this);
    }

    int sd = 0;

    @Override
    public void onClick(View view) {
        if (findViewById(R.id.btnReload).equals(view)) {
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
            view.startAnimation(hyperspaceJumpAnimation);
            sd++;
            if (sd % 2 == 0) {
                Knotify.getInstance().show(Knotify.MessageType.MSG_TRY_CONNECT_TO_SERVER);
            } else {
                Knotify.getInstance().show(Knotify.MessageType.MSG_NO_NEWS);
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e(LOGTAG, "Failed to unbind from the service");
        }
    }

    private void doBindService() {
        try {
            bindService(new Intent(this, ServiceCheckServer.class), serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception ex) {
            Log.d(LOGTAG, "errro on doBindService");
        }
    }

    private void doUnbindService() {
        if (isBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (serviceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, ServiceCheckServer.MessageType.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = messenger;
                    serviceMessenger.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private ArrayList<NewsModel> generateData() {
        ArrayList<NewsModel> models = (ArrayList) NewsDatasource.getInstance().getAllContents();
        return models;
    }

    /**
     * check is service SCheckServer running.
     *
     * @return boolean
     */
    public boolean isSCheckServerRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (ServiceCheckServer.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    /**
     * Send data to the service
     */
    private void sendMessageToService() {
        if (isBound) {
            if (serviceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, ServiceCheckServer.MessageType.MSG_1, 101, 0);
                    msg.replyTo = messenger;
                    serviceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
        serviceMessenger = new Messenger(service);
        try {
            Message msg = Message.obtain(null, ServiceCheckServer.MessageType.MSG_REGISTER_CLIENT);
            msg.replyTo = messenger;
            serviceMessenger.send(msg);
            isBound = true;
        } catch (RemoteException e) {
        }
    }

    public void onServiceDisconnected(ComponentName arg0) {
        serviceMessenger = null;
        isBound = false;
    }

    public int toDIPMetric(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    /**
     * Handle incoming messages from MyService
     */
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServiceCheckServer.MessageType.MSG_2:
                    String str1 = msg.getData().getString("str1");
                    Log.d(LOGTAG, str1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
