package ng.com.easyworld.easyworld;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Oisrael on 5/1/2018.
 */

public class GetMsgFeedObjectDataList {

    private EasyFeedMessage feedMessages;
    private DbaseHelper db ;

    private static final String ns = null;

    public ArrayList<EasyFeedMessage> GetMsgFeedObjectDataList (StringBuilder sb,Context ctx) throws
            XmlPullParserException, IOException {


        ArrayList<EasyFeedMessage> messages = new ArrayList<>();
        db = new DbaseHelper(ctx);


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


    private ArrayList<EasyFeedMessage> readFeed (XmlPullParser xpp) throws IOException, XmlPullParserException {
        ArrayList<EasyFeedMessage> messages = new ArrayList();


        xpp.require(XmlPullParser.START_TAG, ns, "string");
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            // Starts by looking for the entry tag
            if (name.equals("WallPost")) {
                messages.add(readEntry(xpp));
            } else {

            }
        }
        return messages;

    }


    private EasyFeedMessage readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "WallPost");

        String postId = "";
        String postTitle = "";
        String image= "";
        String video= "";
        String content= "";
        String emailaddress= "";
        String time= "";
        String date= "";
        String longi= "";
        String lati= "";
        String privacy= "";
        String likes= "";
        String comments= "";
        String dislikes= "";
        String sendername= "";
        String photo= "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("PostId")) {
                postId= ReadPostId(parser);
            } else if (name.equals("PostTitle")) {
                postTitle = ReadPostTitle(parser);
            }
            else if (name.equals("Image")) {
                image = ReadImage(parser);
            }
            else if (name.equals("Video")) {
                video = ReadVideo(parser);
            }
            else if (name.equals("Content")) {
                content = ReadContent(parser);
            }
            else if (name.equals("emailaddress")) {
                emailaddress = ReadEmailAddress(parser);
            }
            else if (name.equals("Timee")) {
                time = ReadTime(parser);
            }
            else if (name.equals("Datee")) {
                date = ReadDate(parser);
            }

            else if (name.equals("Longitude")) {
                longi = ReadLongitude(parser);
            }
            else if (name.equals("Latitude")) {
                lati = ReadLatitude(parser);
            }
            else if (name.equals("Privacy")) {
                privacy = ReadPrivacy(parser);
            }
            else if (name.equals("Likes")) {
                likes = ReadLikes(parser);
            }
            else if (name.equals("Comments")) {
                comments = ReadComments(parser);
            }
            else if (name.equals("Dislikes")) {
                dislikes = ReadDislikes(parser);
            }
            else if (name.equals("firstname")) {
                sendername = ReadSenderName(parser);
            }
            else if (name.equals("photo")) {
                photo = ReadPhoto(parser);
            }
            else{}

        }


        //Save this to the database
        DateAndTime dt = db.getLastItemTime();
        //Get the Time the last Item was inserted into the dfatabase


        String AndroidDatee = dt.getDatee();
        String androidTimee = dt.getTim();



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa");

        Date serverT = null;
        Date androidT = null;


        Date serverDate = null;
        Date androidDate = null;
        try {


            serverDate = sdf.parse(date);

            try{

                androidDate = sdf.parse(AndroidDatee);
            }catch(Exception ee){
            long id =    db.insertWallPost(postId,postTitle,image,video,content,emailaddress,time,
                        date,longi,lati,privacy,likes,comments,dislikes,sendername,photo);
            String dommy = "";
            }


            int r = serverDate.compareTo(androidDate);

            if ( r >=0  ){

                long id =   db.insertWallPost(postId,postTitle,image,video,content,emailaddress,time,
                        date,longi,lati,privacy,likes,comments,dislikes,sendername,photo);
                String dommy = "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }





        return new EasyFeedMessage (postId,postTitle,image,video,content,emailaddress,time,
                date,longi,lati,privacy,likes,comments,dislikes,sendername,photo);
    }

    private String ReadPostId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "PostId");
        String postId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "PostId");
        return postId;
    }


    private String ReadPostTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "PostTitle");
        String postTitle = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "PostTitle");
        return postTitle;
    }

    private String ReadImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Image");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Image");
        return latitude;
    }

    private String ReadVideo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Video");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Video");
        return latitude;
    }


    private String ReadContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Content");
        String posterName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Content");
        return posterName;
    }


    private String ReadEmailAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "emailaddress");
        String posterName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "emailaddress");
        return posterName;
    }

    private String ReadSenderName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "firstname");
        String posterName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "firstname");
        return posterName;
    }

    private String ReadTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Timee");
        String dateTimeTxt = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Timee");
        return dateTimeTxt;
    }

    private String ReadDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Datee");
        String datee = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Datee");
        return datee;
    }

    private String ReadLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Longitude");
        String longitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Longitude");
        return longitude;
    }


    private String ReadLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Latitude");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Latitude");
        return latitude;
    }


    private String ReadPrivacy(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Privacy");
        String video = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Privacy");
        return video;
    }


    private String ReadLikes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Likes");
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Likes");
        return content;
    }

    private String ReadComments(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Comments");
        String likes = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Comments");
        return likes;
    }


    private String ReadDislikes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Dislikes");
        String comments = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Dislikes");
        return comments;
    }
    private String ReadPhoto(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "photo");
        String phot = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "photo");
        return phot;
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
