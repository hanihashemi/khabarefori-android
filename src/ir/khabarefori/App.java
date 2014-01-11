package ir.khabarefori;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hani on 1/11/14.
 */
public class App {
    public static int getNewUpdateVersionCode(String url) {
        try {
            URL yahoo = new URL(url);
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();

            return Integer.parseInt(a.toString());
        } catch (Exception ex) {
            return -1;
        }
    }
}
