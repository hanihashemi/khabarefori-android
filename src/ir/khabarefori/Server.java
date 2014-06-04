package ir.khabarefori;

import com.google.gson.Gson;
import ir.khabarefori.AppPath;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.json.models.NewsJsonModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Hani on 5/30/14.
 */
public class Server {

    public static NewsJsonModel getLastNews() throws IOException {
        URL url = new URL(AppPath.Network.getNewNewsPage(NewsTable.getInstance().getLastId()));
        InputStream inputStream = url.openConnection().getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(
                inputStream, "UTF8"));

        return new Gson().fromJson(buffer, NewsJsonModel.class);
    }
}
