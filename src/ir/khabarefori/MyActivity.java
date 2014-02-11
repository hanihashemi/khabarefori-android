package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.database.SqlLite;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.listview.Item;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.service.SCheckServer;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(this, generateData() ,listView);
        listView.setAdapter(adapter);

        ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(this);

        if (!isSCheckServerRunning())
            startService(new Intent(this, SCheckServer.class));

        SqlLite.getInstance();

        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));
        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));
        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));
        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));
        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));
        NewsDatasource.getInstance().add(new NewsModel("عنوان", "متن متن متن متن متن متن متن متن ", "link", "1392/08/02", "image", true));

        List<NewsModel> models = NewsDatasource.getInstance().getAllContents();
        for (int i = 0; i < models.size(); i++)
            Log.d("Hani", models.get(i).getSubject() + " " + models.get(i).getContext());
    }




    private ArrayList<Item> generateData(){
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("سوریه تهدید کرد مذاکرات ژنو را ترک می‌کند","ولید معلم، وزیر خارجه سوریه، تهدید کرده است که حکومت سوریه ممکن است مذاکرات ژنو را ترک کند.این اقدام پس از آن انجام گرفته است نمایندگان دولت سوریه با اخضر ابراهیمی، نماینده ویژه سازمان ملل ملاقات کرده‌اند.پیش از این مذاکره رو در روی نمایندگان دولت سوریه و مخالفان که قرار بود امروز انجام شود، به تعویق افتاده و نمایندگان دولت سوریه و مخالفان به جای مذاکره رو در رو در یک اتاق، از طریق نماینده ویژه سازمان ملل با یکدیگر گفتگو می‌کنند."));
        items.add(new Item("کره‌شمالی 'نامه آشتی' برای کره‌جنوبی فرستاد","کره شمالی در نامه‌ای سرگشاده به همسایه جنوبی‌اش پیشنهاد آشتی داده و سئول را از هرگونه \"حرکت نظامی خصومت‌آمیز\" بر حذر داشته است.این نامه در آستانه مانور مشترک آمریکا و کره‌جنوبی ارسال شده که قرار است ماه آینده در آبهای میان دو کشور برگزار شود."));
        items.add(new Item("روحانی: درهای اقتصاد ایران به روی جهان باز است","به گزارش خبرگزاری فارس، «حسن روحانی» رئیس‌جمهوری ایران روز چهارشنبه در مصاحبه با شبکه «سی ان ان» در حاشیه اجلاس مجمع جهانی اقتصاد در «داووس» سوئیس گفت که دولت ایران سانتریفیوژهای کنونی را بر نمی‌چیند.وی با این حال ادامه داد: ما آمادگی داریم اطمینان دهیم که هیچ نگرانی درباره برنامه (هسته‌ای) وجود نداشته باشد.رئیس جمهوری ایران که به داووس سوئیس سفر کرده است در گفتگو با شبکه آرتی‌اس این کشور نیز ابراز داشت که بیش از 3 دهه دشمنی بین ایران و ایالات متحده در صورت تلاش دو طرف می‌تواند به دوستی بدل شود."));
        items.add(new Item("سوریه تهدید کرد مذاکرات ژنو را ترک می‌کند","ولید معلم، وزیر خارجه سوریه، تهدید کرده است که حکومت سوریه ممکن است مذاکرات ژنو را ترک کند.این اقدام پس از آن انجام گرفته است نمایندگان دولت سوریه با اخضر ابراهیمی، نماینده ویژه سازمان ملل ملاقات کرده‌اند.پیش از این مذاکره رو در روی نمایندگان دولت سوریه و مخالفان که قرار بود امروز انجام شود، به تعویق افتاده و نمایندگان دولت سوریه و مخالفان به جای مذاکره رو در رو در یک اتاق، از طریق نماینده ویژه سازمان ملل با یکدیگر گفتگو می‌کنند."));
        items.add(new Item("کره‌شمالی 'نامه آشتی' برای کره‌جنوبی فرستاد","کره شمالی در نامه‌ای سرگشاده به همسایه جنوبی‌اش پیشنهاد آشتی داده و سئول را از هرگونه \"حرکت نظامی خصومت‌آمیز\" بر حذر داشته است.این نامه در آستانه مانور مشترک آمریکا و کره‌جنوبی ارسال شده که قرار است ماه آینده در آبهای میان دو کشور برگزار شود."));
        items.add(new Item("روحانی: درهای اقتصاد ایران به روی جهان باز است","به گزارش خبرگزاری فارس، «حسن روحانی» رئیس‌جمهوری ایران روز چهارشنبه در مصاحبه با شبکه «سی ان ان» در حاشیه اجلاس مجمع جهانی اقتصاد در «داووس» سوئیس گفت که دولت ایران سانتریفیوژهای کنونی را بر نمی‌چیند.وی با این حال ادامه داد: ما آمادگی داریم اطمینان دهیم که هیچ نگرانی درباره برنامه (هسته‌ای) وجود نداشته باشد.رئیس جمهوری ایران که به داووس سوئیس سفر کرده است در گفتگو با شبکه آرتی‌اس این کشور نیز ابراز داشت که بیش از 3 دهه دشمنی بین ایران و ایالات متحده در صورت تلاش دو طرف می‌تواند به دوستی بدل شود."));
        return items;
    }

    /**
     * check is service SCheckServer running.
     * @return boolean
     */
    public boolean isSCheckServerRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (SCheckServer.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    @Override
    public void onClick(View view) {
        if (findViewById(R.id.btnReload).equals(view))
        {
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
            view.startAnimation(hyperspaceJumpAnimation);
        }
    }

}
