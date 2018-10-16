package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 8/24/2018.
 */

public class DateAndTime {

  private  String Dat;
  private String Tim;

    DateAndTime (){

    }
    DateAndTime (String dat,String tim){
        this.Dat = dat;
        this.Tim = tim;
    }

    public String  getDatee(){
        return Dat;
    }
    public String getTim(){
        return Tim;
    }

  }
