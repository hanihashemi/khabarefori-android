package ir.khabarefori.notify;

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

/**
 * Created by hani on 2/11/14.
 */
public class Knotify {

    private static boolean isOpen = false;
    private MyActivity activity;

    public Knotify(MyActivity activity) {
        this.activity = activity;
    }

    public void show() {

        toggleLayoutKnotify();

        TextSwitcher valueView = (TextSwitcher) activity.findViewById(R.id.txtMessage);

        try {
            valueView.setFactory(new ViewSwitcher.ViewFactory() {

                public View makeView() {
                    TextView myText = new TextView(activity.getApplicationContext());
                    myText.setGravity(Gravity.CENTER);
                    myText.setTextColor(activity.getBaseContext().getResources().getColor(R.color.knotify_text));
                    myText.setTextAppearance(activity.getApplicationContext() , android.R.style.TextAppearance_DeviceDefault_Medium);
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
        valueView.setText("Hani");
    }

    private void toggleLayoutKnotify() {
        LinearLayout layoutKnotify = (LinearLayout) activity.findViewById(R.id.layoutKnotify);

        if (isOpen) {
            isOpen = false;

            DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(5), activity.toDIPMetric(35));
            dropDownAnim.setDuration(500);
            layoutKnotify.startAnimation(dropDownAnim);
        } else {
            isOpen = true;

            DropDownAnim dropDownAnim = new DropDownAnim(layoutKnotify, activity.toDIPMetric(35), activity.toDIPMetric(5));
            dropDownAnim.setDuration(500);
            layoutKnotify.startAnimation(dropDownAnim);
        }
    }

}
