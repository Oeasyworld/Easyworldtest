package ng.com.easyworld.easyworld;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Oisrael on 5/17/2018.
 */

public class GetPostResDatalist {

    ResponsObj currentUser;

    private static final String ns = null;




    public ArrayList<ResponsObj> GetPostResDatalit (StringBuilder sb) throws
            XmlPullParserException, IOException {


        ArrayList<ResponsObj> easyUsers = new ArrayList<>();


        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        XmlPullParser xpp2 = factory.newPullParser();

        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xpp2.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

        xpp.setInput(new StringReader(sb.toString()));
        xpp.nextTag();
        return readFeed(xpp);



    }


    private ArrayList<ResponsObj> readFeed (XmlPullParser xpp) throws IOException, XmlPullParserException {
        ArrayList<ResponsObj> easyUsers = new ArrayList();


        xpp.require(XmlPullParser.START_TAG, ns, "string");
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            // Starts by looking for the entry tag
            if (name.equals("WallPost")) {
                easyUsers.add(readEntry(xpp));
            } else {

            }
        }
        return easyUsers;

    }









    private ResponsObj readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "WallPost");
        String res = null;



        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Respons")) {
                res= ReadPwrd(parser);
            }

            else{}



        }
        return new ResponsObj (res);
    }


    private String ReadPwrd(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Respons");
        String bcpwrd = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Respons");
        return bcpwrd;
    }



    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }




}
