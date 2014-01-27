package ir.khabarefori.listview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ir.khabarefori.R;

import java.util.ArrayList;

/**
 * Created by hani on 1/24/14.
 */
public class ListViewAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public ListViewAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.list_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.list_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getDescription());

        //Set anim
        LinearLayout itemLayout = (LinearLayout) rowView.findViewById(R.id.itemLayout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                DropDownAnim animation = new DropDownAnim(view, 300, view.getHeight());
                animation.setDuration(500);
                view.startAnimation(animation);

            }
        });

        // 5. retrn rowView
        return rowView;
    }
}

class DropDownAnim extends Animation {
    private final int targetHeight;
    private final View view;
    private final int defaultHeight;

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