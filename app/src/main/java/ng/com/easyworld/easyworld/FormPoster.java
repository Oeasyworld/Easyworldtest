package ng.com.easyworld.easyworld;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by oisrael on 20-Dec-17.
 */

public class FormPoster {


    private URL url;  // from Chapter 5, Example 5-8
    private QueryString query = new QueryString() ;
    public FormPoster (URL url)
    {
        if (!url.getProtocol().toLowerCase().startsWith("http"))
        {
            throw new IllegalArgumentException(
                    "Posting only works for http URLs");
        }    this.url = url;
    }
    public void add(String name, String value) {
        query.add(name, value);
    }

    public URL getURL() {
        return this.url;
    }

    public InputStream post() throws IOException {
        // open the connection and prepare it to POST
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        // Don't use a cached copy.
        uc.setUseCaches(false);

        try  {
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
            out.write(query.toString());
            out.write("\r\n");
            out.flush();
            out.close();

            DataInputStream din = new DataInputStream(uc.getInputStream());

        }catch (Exception e){}

        // Return the response
        return uc.getInputStream();
    }

    private  class QueryString {

        private String query = "";



        public void add(String name, String value) {
            query += "&";
            encode(name, value);
        }

        private void encode(String name, String value) {
            try {
                query += URLEncoder.encode(name, "UTF-8");
                query += "=";
                query += URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Broken VM does not support UTF-8");
            }
        }

        public String getQuery() {
            return query;
        }

        public String toString() {
            return getQuery();
        }
    }

}
