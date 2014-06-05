package ir.khabarefori.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.AppPath;
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
        TextView txtReadMore = (Button) rowView.findViewById(R.id.txtReadMore);
        TextView txtShare = (Button) rowView.findViewById(R.id.txtShare);
        TextView txtDateTime = (TextView) rowView.findViewById(R.id.txtDatetime);

        //set date time
        txtDateTime.setText(itemsArrayList.get(position).formatToYesterdayOrToday() + " ");

        //set visible today news line
        if (!itemsArrayList.get(position).isNew())
            lineBreakingNews.setVisibility(View.INVISIBLE);


        //set onclick link
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemsArrayList.get(position).getLink() == null || itemsArrayList.get(position).getLink().equals("") || itemsArrayList.get(position).getLink().equals("#")) {
                    Toast.makeText(context, "منبع خبر در دسترس نیست", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(itemsArrayList.get(position).getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "خبرفوری ," + itemsArrayList.get(position).getSubject() + " " + AppPath.Network.host;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "خبرفوری");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

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
            lastItemSelected.viewUiUpdate = rowView;
            itemButtonStatus(rowView, true);
        } else
            txtContext.setText(itemsArrayList.get(position).getShortContext());


        return rowView;
    }

    private void itemButtonStatus(View view, boolean visible) {
        TextView txtReadMore = (Button) view.findViewById(R.id.txtReadMore);
        TextView txtPoint = (TextView) view.findViewById(R.id.txtPoint);
        TextView txtShare = (Button) view.findViewById(R.id.txtShare);

        if (visible) {
            txtShare.setVisibility(View.VISIBLE);
            txtPoint.setVisibility(View.VISIBLE);
            txtReadMore.setVisibility(View.VISIBLE);
        } else {
            txtShare.setVisibility(View.INVISIBLE);
            txtPoint.setVisibility(View.INVISIBLE);
            txtReadMore.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        TextSwitcher txtContext = (TextSwitcher) view.findViewById(R.id.context);

        if (lastItemSelected != null && lastItemSelected.isOpen) {
            try {
                itemButtonStatus(lastItemSelected.view, false);
            } catch (Exception ex) {

            }

            try {
                itemButtonStatus(lastItemSelected.viewUiUpdate, false);
            } catch (Exception ex) {

            }

            try {
                lastItemSelected.objContext.setText(lastItemSelected.shortText);
                lastItemSelected.objUiUpdateContext.setText(lastItemSelected.shortText);
            } catch (Exception ex) {

            }

            try {
                lastItemSelected.objContext.setText(lastItemSelected.shortText);
                lastItemSelected.objUiUpdateContext.setText(lastItemSelected.shortText);
            } catch (Exception ex) {

            }

            if (lastItemSelected.position == position) {
                lastItemSelected.isOpen = false;
                return;
            }
        }

        openItem(view, txtContext,
                itemsArrayList.get(position).getContext(),
                itemsArrayList.get(position).getShortContext(),
                position);
    }

    private void openItem(View view, TextSwitcher txtContext, String value, String valueShort, int position) {
        lastItemSelected = new LastItem();
        lastItemSelected.isOpen = true;
        lastItemSelected.objContext = txtContext;
        lastItemSelected.objUiUpdateContext = txtContext;
        lastItemSelected.shortText = valueShort;
        lastItemSelected.position = position;
        lastItemSelected.view = view;

        itemButtonStatus(view, true);

        txtContext.setText(value);
    }

    private class LastItem {
        public TextSwitcher objContext;
        public TextSwitcher objUiUpdateContext;
        public View view;
        public View viewUiUpdate;
        public String shortText = "";
        public boolean isOpen = false;
        public int position = -1;
    }
}