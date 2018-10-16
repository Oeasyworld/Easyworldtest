package ng.com.easyworld.easyworld;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oisrael on 15-Feb-17.
 */

public class LoadFeedData extends AsyncTask<String, Integer, String> {

    private final String mUrl = "http://picasaweb.google.com/data/feed/api/all?kind=photo&q=" +
                                    "sunset%20landscape&alt=json&max-results=20&thumbsize=144c";



    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}

