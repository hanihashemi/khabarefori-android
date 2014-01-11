package ir.khabarefori;

/**
 * Created by hani on 1/11/14.
 */
public class AppPath {
    public static class Local {

    }

    public static class Network {
        private static String host = "http://khabarefori.ir/";

        public static String getNewNewsPage() {
            return host + "api/get_news.php";
        }

        public static String getUpdateVersion() {
            return host + "api/get_last_update_mobile.php";
        }
    }
}
