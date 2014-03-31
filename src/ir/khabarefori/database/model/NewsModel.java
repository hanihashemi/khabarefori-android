package ir.khabarefori.database.model;

import ir.khabarefori.lib.datetime.Roozh;

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
    private String image;
    private boolean is_breaking_news;

    public NewsModel() {

    }

    public NewsModel(int serverID, String subject, String context, String link, String datetime, String image, boolean is_breaking_news) {
        this.serverID = serverID;
        this.subject = subject;
        this.context = context;
        this.link = link;
        this.datetime = datetime;
        this.is_breaking_news = is_breaking_news;
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


    public void setPersianDatetime(String datetime) {
        try {
            String[] strDate = datetime.split(" ")[0].split("-");
            int[] intDate = new int[3];
            for (int i = 0; i < strDate.length; i++)
                intDate[i] = Integer.parseInt(strDate[i]);

            Roozh roozh = new Roozh();
            roozh.GregorianToPersian(intDate[0], intDate[1], intDate[2]);
            this.datetime = roozh.toString();
        } catch (Exception ex) {
            this.datetime = "";
        }
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

    public void setIsBreakingNewsParamBoolean(boolean is_breaking_news) {
        this.is_breaking_news = is_breaking_news;
    }

    public String getContextShort() {

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
}
