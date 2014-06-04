package ir.khabarefori.json.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 1/11/14.
 */
public class NewsJsonModel {

    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public void addNews(News newsData) {
        if (news == null)
            news = new ArrayList<News>();
        news.add(newsData);
    }

    public class News {
        public int id;
        public String subject;
        public String context;
        public String datetime;
        public String link;
        public int is_breaking_news;

        public boolean getIsBreakingNews() {
            return is_breaking_news == 1 ? true : false;
        }

    }
}

