package ir.khabarefori.notify;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by hani on 2/21/14.
 */
public class DropDownAnim extends Animation{
    private final int targetHeight;
    private final View view;
    private final int defaultHeight;
    private final String LOGTAG = "DropDownAnim";



    public DropDownAnim(View view, int targetHeight, int defaultHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.defaultHeight = defaultHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        newHeight = (int) ((targetHeight - defaultHeight) * interpolatedTime + defaultHeight);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }


    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
