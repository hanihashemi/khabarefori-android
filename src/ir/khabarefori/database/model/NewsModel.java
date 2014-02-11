package ir.khabarefori.database.model;

/**
 * Created by hani on 2/3/14.
 */
public class NewsModel {
    private int id;
    private String subject;
    private String context;
    private String link;
    private String datetime;
    private String image;
    private boolean is_breaking_news;

    public NewsModel() {

    }

    public NewsModel(String subject, String context, String link, String datetime, String image, boolean is_breaking_news) {
        this.subject = subject;
        this.context = context;
        this.link = link;
        this.datetime = datetime;
        this.image = image;
        this.is_breaking_news = is_breaking_news;
    }

    public int getid() {
        return id;
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

    public void setIsBreakingNews(int is_breaking_news) {
        this.is_breaking_news = is_breaking_news != 0 ? true : false;
    }
}
