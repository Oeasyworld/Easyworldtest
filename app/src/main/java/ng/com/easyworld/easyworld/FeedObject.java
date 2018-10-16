package ng.com.easyworld.easyworld;

/**
 * Created by oisrael on 02-Nov-17.
 */

public class FeedObject {
    private StringBuilder myxml;
    private Boolean er = true;

    public FeedObject(){

    }

    public StringBuilder getMyXml(){
        return myxml;
    }

    public Boolean getErr(){
        return er;
    }

    public void SetMyXml(StringBuilder sb){
        myxml = sb;
    }
    public void SetErr(Boolean err){
        er = err;
    }
}
