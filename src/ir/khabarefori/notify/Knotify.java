package ir.khabarefori.notify;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import ir.khabarefori.MyActivity;
import ir.khabarefori.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hani on 2/11/14.
 */
public class Knotify {
    private static Knotify instance;

    public static boolean isOpen = false;
    private final String LOGTAG = "Knotify";
    protected MyActivity activity;
    private Timer timerWaiting = null;
    private Handler handlerFoldLayoutKnotify = new Handler() {
        public void handleMessage(Message msg) {
            hide();
        }
    };

    public static Knotify getInstance() {
        if (instance == null)
            instance = new Knotify();

        return instance;
    }

    public static void updateMainActivity(MyActivity activity) {
        getInstance().activity = activity;
    }

    public Knotify() {

    }

    /**
     * TODO this method should get to myactivity
     */
    private void refreshBtnReload() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.refreshBtnReload();
            }
        });
    }

    /**
     * TODO this method should get to myactivity
     */
    private void refreshListView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.refreshListView();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public void show(int type) {

        if (timerWaiting != null) {
            timerWaiting.cancel();
            timerWaiting = null;
        }

        refreshBtnReload();

        switch (type) {
            case MessageType.MSG_TRY_CONNECT_TO_SERVER:
                setMessage(
                        activity.getString(R.string.try_connect_to_server)
                        , false);
                break;
            case MessageType.MSG_NO_NEWS:
                setMessage(
                        activity.getString(R.string.no_news)
                        , true);
                break;
            case MessageType.MSG_NO_INTERNET:
                if (isOnline())
                    setMessage(
                            activity.getString(R.string.no_server)
                            , false);
                else
                    setMessage(
                            activity.getString(R.string.no_internet)
                            , false);
                break;
            case MessageType.MSG_NEW_NEWS_UPDATED:
                setMessage(activity.getString(R.string.new_news_updated), true);
                refreshListView();
                break;

        }
    }

    public void hide() {
        if (isOpen) {
            foldLayoutKnotify();
            setTextMessage("");
        }
    }

    private void setMessage(String value, boolean enableTimer) {
        expandLayoutKnotify();
        setTextMessage(value);
        if (enableTimer) {
            timerWaiting = new Timer();
            timerWaiting.schedule(new TimerTask() {
                @Override
                public void run() {
                    handlerFoldLayoutKnotify.obtainMessage(1).sendToTarget();
                }
            }, 10000);
        }
    }

    private void setTextMessage(final String value) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextSwitcher valueView = (TextSwitcher) activity.findViewById(R.id.txtMessage);

                try {
                    valueView.setFactory(new ViewSwitcher.ViewFactory() {

                        public View makeView() {
                            TextView myText = new TextView(activity.getApplicationContext());
                            myText.setGravity(Gravity.CENTER);
                            myText.setTextColor(activity.getBaseContext().getResources().getColor(R.color.knotify_text));
                            myText.setTextAppearance(activity.getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
                            return myText;
                        }
                    });
                    Animation in = AnimationUtils.loadAnimation(activity.getApplicationContext(), android.R.anim.slide_in_left);
                    Animation out = AnimationUtils.loadAnimation(activity.getApplicationContext(), android.R.anim.slide_out_right);

                    valueView.setInAnimation(in);
                    valueView.setOutAnimation(out);
                } catch (Exception ex) {
                }

                valueView.setText(value);
            }
        });
    }

    private void expandLayoutKnotify() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isOpen) {
                    isOpen = true;
                    LinearLayout layoutKnotify = (LinearLayout) activity.findViewById(R.id.layoutKnotify);

                    DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(35), activity.toDIPMetric(5));
                    dropDownAnim.setDuration(500);
                    layoutKnotify.startAnimation(dropDownAnim);
                }
            }
        });

    }

    private void foldLayoutKnotify() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isOpen) {
                    isOpen = false;

                    LinearLayout layoutKnotify = (LinearLayout) activity.findViewById(R.id.layoutKnotify);

                    DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(5), activity.toDIPMetric(35));
                    dropDownAnim.setDuration(500);
                    layoutKnotify.startAnimation(dropDownAnim);
                }
            }
        });
    }

    public class MessageType {
        public static final int MSG_TRY_CONNECT_TO_SERVER = 0;
        public static final int MSG_NO_INTERNET = 1;
        public static final int MSG_NO_NEWS = 2;
        public static final int MSG_NEW_NEWS_UPDATED = 3;
    }
}