package ng.com.easyworld.easyworld;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by oisrael on 22-Dec-17.
 */

public class GetProfileObjectDataLst {

    EasyUserProfile currentUser;

    private static final String ns = null;

    public ArrayList<EasyUserProfile> GetObjectDataList (StringBuilder sb) throws
            XmlPullParserException, IOException {


        ArrayList<EasyUserProfile> easyUsers = new ArrayList<>();


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


    private ArrayList<EasyUserProfile> readFeed (XmlPullParser xpp) throws IOException, XmlPullParserException {
        ArrayList<EasyUserProfile> easyUsers = new ArrayList();


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

    private EasyUserProfile readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "EwMem20ber17tbl");




        String uid = "";
        String uname= "";
        String fname= "";
        String sname= "";
        String lname= "";
        String oldname= "";
        String oldnickname= "";
        String emailaddress= "";
        String kokoro= "";
        String phone= "";
        String dateofbirth= "";
        String addres= "";
        String city= "";
        String statee= "";
        String country= "";
        String latitude= "";
        String longitude= "";
        String occupation= "";
        String higestlevelofeducation= "";
        String institutionname= "";
        String beginyr= "";
        String endyr= "";
        String highestqualificatn= "";
        String gender= "";
        String photo= "";
        String space1= "";
        String space2= "";
        String space3= "";
        String space4= "";
        String space5= "";


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("uid")) {
                uid= ReadUid(parser);
            } else if (name.equals("username")) {
                uname = ReadUname(parser);
            }
            else if (name.equals("firstname")) {
                fname = ReadFname(parser);
            }
            else if (name.equals("surname")) {
                sname = ReadSname(parser);
            }
            else if (name.equals("lastname")) {
                lname = ReadLname(parser);
            }
            else if (name.equals("oldname")) {
                oldname = ReadOldname(parser);
            }
            else if (name.equals("oldnickname")) {
                oldnickname = ReadOldnickname(parser);
            }
            else if (name.equals("emailaddress")) {
                emailaddress = ReadEmailaddress(parser);
            }
            else if (name.equals("kokoro")) {
                kokoro = ReadKokoro(parser);
            }
            else if (name.equals("phone")) {
                phone = ReadPhone(parser);
            }
            else if (name.equals("dateofbirth")) {
                dateofbirth = ReadDateofbirth(parser);
            }
            else if (name.equals("addres")) {
                addres = ReadAddres(parser);
            }
            else if (name.equals("city")) {
                city = ReadCity(parser);
            }
            else if (name.equals("statee")) {
                statee = ReadStatee(parser);
            }
            else if (name.equals("country")) {
                country = ReadCountry(parser);
            }
            else if (name.equals("latitude")) {
                latitude = ReadLatitude(parser);
            }
            else if (name.equals("longitude")) {
                longitude = ReadLongitude(parser);
            }
            else if (name.equals("occupation")) {
                occupation = ReadOccupation(parser);
            }
            else if (name.equals("higestlevelofeducation")) {
                higestlevelofeducation = ReadHigestlevelofeducation(parser);
            }
            else if (name.equals("institutionname")) {
                institutionname = ReadInstitutionname(parser);
            }
            else if (name.equals("beginyr")) {
                beginyr = ReadBeginyr(parser);
            }
            else if (name.equals("endyr")) {
                endyr = ReadEndyr(parser);
            }
            else if (name.equals("highestqualificatn")) {
                highestqualificatn = ReadHighestqualificatn(parser);
            }
            else if (name.equals("gender")) {
                gender = ReadGender(parser);
            }
            else if (name.equals("photo")) {
                photo = ReadPhoto(parser);
            }
            else if (name.equals("space1")) {
                space1 = ReadSpace1(parser);
            }
            else if (name.equals("space2")) {
                space2 = ReadSpace2(parser);
            }
            else if (name.equals("space3")) {
                space3 = ReadSpace3(parser);
            }
            else if (name.equals("space4")) {
                space4 = ReadSpace4(parser);
            }
            else if (name.equals("space5")) {
                space5 = ReadSpace5(parser);
            }
            else{}

        }
        return new EasyUserProfile (uid,uname,fname,sname,lname,oldname,oldnickname,
                emailaddress,kokoro,phone,dateofbirth,addres,city,
                statee,country,latitude,longitude,occupation,higestlevelofeducation,
                institutionname,beginyr,endyr,highestqualificatn,gender,photo,space1,
                space2,space3,space4,space5);
    }


    private String ReadUid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "uid");
        String uid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "uid");
        return uid;
    }


    private String ReadUname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "username");
        String uname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "username");
        return uname;
    }



    private String ReadFname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "firstname");
        String fname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "firstname");
        return fname;
    }


    private String ReadSname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "surname");
        String sname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "surname");
        return sname;
    }

    private String ReadLname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "lastname");
        String lname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lastname");
        return lname;
    }


    private String ReadOldname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "oldname");
        String oldname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "oldname");
        return oldname;
    }


    private String ReadOldnickname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "oldnickname");
        String oldnickname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "oldnickname");
        return oldnickname;
    }


    private String ReadEmailaddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "emailaddress");
        String emailaddress = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "emailaddress");
        return emailaddress;
    }


    private String ReadKokoro(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kokoro");
        String kokoro = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kokoro");
        return kokoro;
    }


    private String ReadPhone(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "phone");
        String phone = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "phone");
        return phone;
    }

    private String ReadDateofbirth(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dateofbirth");
        String dateofbirth = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dateofbirth");
        return dateofbirth;
    }


    private String ReadAddres(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "addres");
        String addres = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "addres");
        return addres;
    }


    private String ReadCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "city");
        String city = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "city");
        return city;
    }


    private String ReadStatee(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "statee");
        String statee = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "statee");
        return statee;
    }

    private String ReadCountry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "country");
        String country = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "country");
        return country;
    }


    private String ReadLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "latitude");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "latitude");
        return latitude;
    }


    private String ReadLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "longitude");
        String longitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "longitude");
        return longitude;
    }


    private String ReadOccupation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "occupation");
        String occupation = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "occupation");
        return occupation;
    }


    private String ReadHigestlevelofeducation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "higestlevelofeducation");
        String higestlevelofeducation = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "higestlevelofeducation");
        return higestlevelofeducation;
    }

    private String ReadInstitutionname(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "institutionname");
        String institutionname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "institutionname");
        return institutionname;
    }

    private String ReadBeginyr(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "beginyr");
        String beginyr = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "beginyr");
        return beginyr;
    }

    private String ReadEndyr(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "endyr");
        String endyr = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "endyr");
        return endyr;
    }

    private String ReadHighestqualificatn(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "highestqualificatn");
        String highestqualificatn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "highestqualificatn");
        return highestqualificatn;
    }

    private String ReadGender(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "gender");
        String gender = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gender");
        return gender;
    }

    private String ReadPhoto(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "photo");
        String photo = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "photo");
        return photo;
    }

    private String ReadSpace1(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "space1");
        String space1 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "space1");
        return space1;
    }

    private String ReadSpace2(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "space2");
        String space2 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "space2");
        return space2;
    }

    private String ReadSpace3(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "space3");
        String space3 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "space3");
        return space3;
    }

    private String ReadSpace4(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "space4");
        String space4 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "space4");
        return space4;
    }

    private String ReadSpace5(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "space5");
        String space5 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "space5");
        return space5;
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
