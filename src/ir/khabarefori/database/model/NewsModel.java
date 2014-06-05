package ir.khabarefori.database.model;

import android.util.Log;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.helper.DateTime.GeneralFormat;
import ir.khabarefori.helper.DateTime.HDateTime;
import ir.khabarefori.helper.DateTime.Roozh;
import ir.khabarefori.json.models.NewsJsonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hani on 2/3/14.
 */
public class NewsModel {
    private int id;
    private int serverID;
    private String subject;
    private String context;
    private String link;
    private String datetime;
    private String persian_datetime;
    private String image;
    private boolean is_breaking_news;

    public NewsModel() {

    }

    public int getid() {
        return id;
    }

    public int getServerID() {
        return serverID;
    }

    public String getSubject() {
        return subject;
    }

    public String getContext() {
        return context;
    }

    public String getLink() {
        return link;
    }


    public void setPersianDatetimeAndConvert(String datetime) {
        try {
            GeneralFormat generalFormat = HDateTime.convertDatabaseFormatToGeneralFormat(datetime);

            Roozh roozh = new Roozh();
            roozh.GregorianToPersian(generalFormat.Year, generalFormat.Month, generalFormat.Day);
            this.persian_datetime = roozh.toString();
        } catch (Exception ex) {
            this.persian_datetime = "";
        }
    }

    public void setPersianDatetime(String datetime) {
        this.persian_datetime = datetime;
    }

    public String getDatetime() {

        return datetime;
    }

    public String getImage() {
        return image;
    }

    public boolean getIsBreakingNews() {
        return is_breaking_news;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public void setIsBreakingNewsParamInt(int is_breaking_news) {
        this.is_breaking_news = is_breaking_news == 1 ? true : false;
    }

    public String getPersianDateTime() {
        return this.persian_datetime;
    }

    public void setIsBreakingNewsParamBoolean(boolean is_breaking_news) {
        this.is_breaking_news = is_breaking_news;
    }

    public String getShortContext() {

        if (context == null)
            return "null";

        String text = context.trim();
        String[] texts = text.split(" ");
        String result = "";

        if (texts.length <= 30)
            return text;

        for (int i = 0; i < 30; i++)
            result += texts[i] + " ";

        return result + "...";
    }

    public boolean isNew() {
        try {
            GeneralFormat newsDate = HDateTime.convertDatabaseFormatToGeneralFormat(this.datetime);

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);

            if (newsDate.Year == year && newsDate.Month == month && newsDate.Day == day)
                return true;
        } catch (Exception ex) {
        }

        return false;
    }

    public String formatToYesterdayOrToday() {
        try {
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(this.datetime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                return " امروز ";
            } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
                return " دیروز ";
            } else {
                return this.getPersianDateTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return this.getDatetime();
        } catch (Exception e){
            return this.getDatetime();
        }
    }

    public static void createRows(NewsJsonModel news) {
        for (int i = 0; i < news.getNews().size(); i++)
            NewsModel.createRow(news.getNews().get(i));
    }

    public static NewsModel createRow(NewsJsonModel.News news) {
        NewsModel model = new NewsModel();
        model.setServerID(news.id);
        model.setSubject(news.subject);
        model.setContext(news.context);
        model.setPersianDatetimeAndConvert(news.datetime);
        model.setDatetime(news.datetime);
        model.setIsBreakingNewsParamBoolean(news.getIsBreakingNews());
        model.setLink(news.link);

        NewsTable.getInstance().add(model);

        return model;
    }
}
