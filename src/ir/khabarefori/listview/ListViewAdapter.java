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
public class ListViewAdapter extends ArrayAdapter<NewsModel> implements AdapterView.OnItemClickListener {

    private static LastItem lastItemSelected;
    private final Context context;
    private final ArrayList<NewsModel> itemsArrayList;
    private final String LOGTAG = "ListViewAdapter";

    public ListViewAdapter(Context context, ArrayList<NewsModel> itemsArrayList, ListView listView) {
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
        TextView txtSubject = (TextView) rowView.findViewById(R.id.subject);
        TextSwitcher txtContext = (TextSwitcher) rowView.findViewById(R.id.context);
        LinearLayout lineBreakingNews = (LinearLayout) rowView.findViewById(R.id.lineBreakingNews);

        //
        if (!itemsArrayList.get(position).getIsBreakingNews())
            lineBreakingNews.setVisibility(View.INVISIBLE);

        txtContext.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(context);
                myText.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
                myText.setGravity(Gravity.RIGHT);
                myText.setTextColor(context.getResources().getColor(R.color.item_text_color));
                return myText;
            }
        });

        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);

        // set the animation type of textSwitcher
        txtContext.setInAnimation(animIn);
        txtContext.setOutAnimation(animOut);

        // 4. Set the text for textView
        txtSubject.setText(itemsArrayList.get(position).getSubject());

        if (lastItemSelected != null && lastItemSelected.isOpen &&
                lastItemSelected.position == position) {
            txtContext.setText(itemsArrayList.get(position).getContext());
            lastItemSelected.objUiUpdateContext = txtContext;
        } else
            txtContext.setText(itemsArrayList.get(position).getContextShort());


        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        TextSwitcher txtContext = (TextSwitcher) view.findViewById(R.id.context);

        if (lastItemSelected != null && lastItemSelected.isOpen) {
            try {
                lastItemSelected.objContext.setText(lastItemSelected.shortText);
                lastItemSelected.objUiUpdateContext.setText(lastItemSelected.shortText);

                lastItemSelected.objContext.setText(lastItemSelected.shortText);
                lastItemSelected.objUiUpdateContext.setText(lastItemSelected.shortText);
            } catch (Exception ex) {
            }

            if (lastItemSelected.position == position) {
                lastItemSelected.isOpen = false;
                return;
            }
        }

        openItem(txtContext,
                itemsArrayList.get(position).getContext(),
                itemsArrayList.get(position).getContextShort(),
                position);
    }

    private void openItem(TextSwitcher txtContext, String value, String valueShort, int position) {
        lastItemSelected = new LastItem();
        lastItemSelected.isOpen = true;
        lastItemSelected.objContext = txtContext;
        lastItemSelected.objUiUpdateContext = txtContext;
        lastItemSelected.shortText = valueShort;
        lastItemSelected.position = position;

        txtContext.setText(value);
    }

    private class LastItem {
        public TextSwitcher objContext;
        public TextSwitcher objUiUpdateContext;
        public String shortText = "";
        public boolean isOpen = false;
        public int position = -1;
    }
}