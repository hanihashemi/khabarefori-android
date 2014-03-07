package ir.khabarefori.notify;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.MyActivity;
import ir.khabarefori.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hani on 2/11/14.
 */
public class Knotify {

    private static boolean isOpen = false;
    private final String LOGTAG = "Knotify";
    private MyActivity activity;
    private Timer timer = null;
    private Handler handlerFoldLayoutKnotify = new Handler() {
        public void handleMessage(Message msg) {
            foldLayoutKnotify();
        }
    };

    public Knotify(MyActivity activity) {
        this.activity = activity;
    }

    public void show(int type, boolean enableTimer) {

        if (timer == null) {
            timer = new Timer();
            timer.cancel();
            timer.purge();
        }

        switch (type) {
            case MessageType.MSG_TRY_CONNECT_TO_SERVER:
                setMessage(
                        activity.getString(R.string.try_connect_to_server)
                        , enableTimer);
                break;
            case MessageType.MSG_NO_NEWS:
                setMessage(
                        activity.getString(R.string.no_news)
                        , enableTimer);
                break;
            case MessageType.MSG_NO_INTERNET:
                setMessage(
                        activity.getString(R.string.no_internet)
                        , enableTimer);
                break;
        }
    }

    public void hide() {
        foldLayoutKnotify();
        setTextMessage("");
    }

    private void setMessage(String value, boolean enableTimer) {
        expandLayoutKnotify();
        setTextMessage(value);
        if (enableTimer)
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    handlerFoldLayoutKnotify.obtainMessage(1).sendToTarget();
                }
            }, 10000);
    }

    private void setTextMessage(String value) {
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

        valueView.setVisibility(View.VISIBLE);
        valueView.setText(value);
    }

    private void expandLayoutKnotify() {
        LinearLayout layoutKnotify = (LinearLayout) activity.findViewById(R.id.layoutKnotify);

        if (!isOpen) {
            isOpen = true;

            DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(35), activity.toDIPMetric(5));
            dropDownAnim.setDuration(500);
            layoutKnotify.startAnimation(dropDownAnim);
        }

    }

    private void foldLayoutKnotify() {
        LinearLayout layoutKnotify = (LinearLayout) activity.findViewById(R.id.layoutKnotify);

        if (isOpen) {
            isOpen = false;

            DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(5), activity.toDIPMetric(35));
            dropDownAnim.setDuration(500);
            layoutKnotify.startAnimation(dropDownAnim);
        }
    }

    public class MessageType {
        public static final int MSG_TRY_CONNECT_TO_SERVER = 0;
        public static final int MSG_NO_INTERNET = 1;
        public static final int MSG_NO_NEWS = 2;
    }
}
