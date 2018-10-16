package ng.com.easyworld.easyworld;

import android.os.StrictMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by oisrael on 11-Apr-17.
 */

public class GetObjectDataLst {

    EasyUser currentUser;

    private static final String ns = null;




    public ArrayList<EasyUser> GetObjectDataList (StringBuilder sb) throws
            XmlPullParserException, IOException {


        ArrayList<EasyUser> easyUsers = new ArrayList<>();


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


    private ArrayList<EasyUser> readFeed (XmlPullParser xpp) throws IOException, XmlPullParserException {
        ArrayList<EasyUser> easyUsers = new ArrayList();


        xpp.require(XmlPullParser.START_TAG, ns, "string");
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            // Starts by looking for the entry tag
            if (name.equals("Ewbc")) {
                easyUsers.add(readEntry(xpp));
            } else {

            }
        }
        return easyUsers;

    }

    private EasyUser readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "Ewbc");
        String bcpwrd = null;
        String bcbusinesname=null;
        String bcperson=null;
        String bcpos=null;
        String bcfield1=null;
        String bcfield2=null;
        String bcfield3=null;
        String bcoffiaddress=null;
        String bcphone1=null;
        String bcphone2=null;
        String bcemailaddress=null;
        String bcwebsit=null;
        String bcdescriptn=null;
        String bclogo=null;
        String bclongitude=null;
        String bclatitude=null;
        String bccategory=null;
        String bcprofilephoto=null;
        String bcregstatus=null;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("bcpwrd")) {
                bcpwrd= ReadPwrd(parser);
            } else if (name.equals("bcbusinesname")) {
                bcbusinesname = ReadBcBusinesName(parser);
            }
            else if (name.equals("bcperson")) {
                bcperson = ReadBcPerson(parser);
            }
            else if (name.equals("bcpos")) {
                bcpos = ReadBcPos(parser);
            }
            else if (name.equals("bcfield1")) {
                bcfield1 = ReadBcField1(parser);
            }
            else if (name.equals("bcfield2")) {
                bcfield2 = ReadBcField2(parser);
            }
            else if (name.equals("bcfield3")) {
                bcfield3 = ReadBcField3(parser);
            }
            else if (name.equals("bcoffiaddress")) {
                bcoffiaddress = ReadBcOffiAddress(parser);
            }
            else if (name.equals("bcphone1")) {
                bcphone1 = ReadBcPhone1(parser);
            }
            else if (name.equals("bcphone2")) {
                bcphone2 = ReadBcPhone2(parser);
            }
            else if (name.equals("bcemailaddress")) {
                bcemailaddress = ReadBcEmailAddres(parser);
            }
            else if (name.equals("bcwebsit")) {
                bcwebsit = ReadWebsite(parser);
            }
            else if (name.equals("bcdescriptn")) {
                bcdescriptn = ReadBcDescriptn(parser);
            }
            else if (name.equals("bclogo")) {
                bclogo = ReadBcLogo(parser);
            }
            else if (name.equals("bclongitude")) {
                bclongitude = ReadBcLongitude(parser);
            }
            else if (name.equals("bclatitude")) {
                bclatitude = ReadLatitude(parser);
            }
            else if (name.equals("bccategory")) {
                bccategory = ReadBcCategory(parser);
            }
            else if (name.equals("bcprofilephoto")) {
                bcprofilephoto = ReadBcProfilePhoto(parser);
            } else if (name.equals("bcregstatus")) {
                bcregstatus = ReadBcregstatus(parser);
            }else{}



        }
        return new EasyUser (bcpwrd,bcbusinesname,bcperson,bcpos,bcfield1,bcfield2,bcfield3,
                bcoffiaddress,bcphone1,bcphone2,bcemailaddress,bcwebsit,bcdescriptn,
                bclogo,bclongitude,bclatitude,bccategory,bcprofilephoto,bcregstatus);
    }


    private String ReadPwrd(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcpwrd");
        String bcpwrd = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcpwrd");
        return bcpwrd;
    }


    private String ReadBcBusinesName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcbusinesname");
        String bcbusinesname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcbusinesname");
        return bcbusinesname;
    }



    private String ReadBcPerson(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcperson");
        String bcperson = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcperson");
        return bcperson;
    }


    private String ReadBcPos(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcpos");
        String bcpos = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcpos");
        return bcpos;
    }

    private String ReadBcField1(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcfield1");
        String bcfield1 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcfield1");
        return bcfield1;
    }


    private String ReadBcField2(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcfield2");
        String bcfield2 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcfield2");
        return bcfield2;
    }


    private String ReadBcField3(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcfield3");
        String bcfield3 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcfield3");
        return bcfield3;
    }


    private String ReadBcOffiAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcoffiaddress");
        String bcoffiaddress = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcoffiaddress");
        return bcoffiaddress;
    }


    private String ReadBcPhone1(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcphone1");
        String bcphone1 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcphone1");
        return bcphone1;
    }


    private String ReadBcPhone2(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcphone2");
        String bcphone2 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcphone2");
        return bcphone2;
    }

    private String ReadBcEmailAddres(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcemailaddress");
        String bcemailaddress = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcemailaddress");
        return bcemailaddress;
    }


    private String ReadWebsite(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcwebsit");
        String bcWebsit = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcwebsit");
        return bcWebsit;
    }


    private String ReadBcDescriptn(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcdescriptn");
        String bcdescriptn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcdescriptn");
        return bcdescriptn;
    }


    private String ReadBcLogo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bclogo");
        String bclogo = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bclogo");
        return bclogo;
    }

    private String ReadBcLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bclongitude");
        String bclongitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bclongitude");
        return bclongitude;
    }


    private String ReadLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bclatitude");
        String bclatitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bclatitude");
        return bclatitude;
    }


    private String ReadBcCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bccategory");
        String bccategory = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bccategory");
        return bccategory;
    }


    private String ReadBcProfilePhoto(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcprofilephoto");
        String bcprofilephoto = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcprofilephoto");
        return bcprofilephoto;
    }


    private String ReadBcregstatus(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "bcregstatus");
        String bcregstatus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bcregstatus");
        return bcregstatus;
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
