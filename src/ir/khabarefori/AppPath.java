package ir.khabarefori;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hani on 1/11/14.
 */
public class AppPath {
    public static class Local {

    }

    public static class Network {
        private static String host = "http://khabarefori.ir/";

        public static String getNewNewsPage() {
            return host + "api_1/get_news.php";
        }

        public static String getNewNewsPage(int lastId) {
            return host + "api_1/get_news.php/?id=" + lastId;
        }

        public static String getNewsSource(int id, String subject) {
            try {
                subject = URLEncoder.encode(subject, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return host + "news_page_source.php?id=" + id + "&title=" + subject;
        }
    }
}
