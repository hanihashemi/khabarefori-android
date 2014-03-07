package ir.khabarefori.listview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.R;
import ir.khabarefori.database.model.NewsModel;

import java.util.ArrayList;

/**
 * Created by hani on 1/24/14.
 */
public class ListViewAdapter extends ArrayAdapter<NewsModel> implements AdapterView.OnItemClickListener{

    private final Context context;
    private final ArrayList<NewsModel> itemsArrayList;

    public ListViewAdapter(Context context, ArrayList<NewsModel> itemsArrayList , ListView listView) {
        super(context, R.layout.list_row, itemsArrayList);

        listView.setOnItemClickListener(this);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.list_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextSwitcher valueView = (TextSwitcher) rowView.findViewById(R.id.value);

        valueView.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(context);
                myText.setTextSize(13);
                myText.setGravity(Gravity.RIGHT);
                myText.setTextColor(context.getResources().getColor(R.color.item_text_color));
                return myText;
            }
        });

        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);

        // set the animation type of textSwitcher
        valueView.setInAnimation(in);
        valueView.setOutAnimation(out);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getSubject());
        valueView.setText(itemsArrayList.get(position).getContextShort());

        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        TextSwitcher valueView = (TextSwitcher) view.findViewById(R.id.value);
        valueView.setText(itemsArrayList.get(position).getContext());
    }
}