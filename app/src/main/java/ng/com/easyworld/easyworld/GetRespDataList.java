package ng.com.easyworld.easyworld;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by oisrael on 14-May-17.
 */

public class GetRespDataList {



    EasyUserReg currentUser;

    private static final String ns = null;




    public ArrayList<EasyUserReg> GetObjectDataLit (StringBuilder sb) throws
            XmlPullParserException, IOException {


        ArrayList<EasyUserReg> easyUsers = new ArrayList<>();


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


    private ArrayList<EasyUserReg> readFeed (XmlPullParser xpp) throws IOException, XmlPullParserException {
        ArrayList<EasyUserReg> easyUsers = new ArrayList();


        xpp.require(XmlPullParser.START_TAG, ns, "string");
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            // Starts by looking for the entry tag
            if (name.equals("EwMem20ber17tbl")) {
                easyUsers.add(readEntry(xpp));
            } else {

            }
        }
        return easyUsers;

    }









    private EasyUserReg readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "EwMem20ber17tbl");
        String bcregstatus = null;



        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Respons")) {
                bcregstatus= ReadPwrd(parser);
            }

           else{}



        }
        return new EasyUserReg (bcregstatus);
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
