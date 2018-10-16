package ng.com.easyworld.easyworld;

/**
 * Created by oisrael on 07-Apr-17.
 */

public class EasyUser {


    String bcpwrd;
    String bcbusinesname;
    String bcperson;
    String bcpos;
    String bcfield1;
    String bcfield2;
    String bcfield3;
    String bcoffiaddress;
    String bcphone1;
    String bcphone2;
    String bcemailaddress;
    String bcWebsit;
    String bcdescriptn;
    String bclogo;
    String bclongitude;
    String bclatitude;
    String bccategory;
    String bcprofilephoto;
    String bcregstatus;



    public EasyUser(String bcpwrd,String bcbusinesname,String bcperson,
                    String bcpos,String bcfield1,String bcfield2,
                    String bcfield3,String bcoffiaddress,String bcphone1,
                    String bcphone2,String bcemailaddress,String bcWebsit,
                    String bcdescriptn,String bclogo,String bclongitude,
                    String bclatitude,String bccategory,String bcprofilephoto,
                    String bcregstatus) {

        this.bcpwrd = bcpwrd;
        this.bcbusinesname = bcbusinesname;
        this.bcperson = bcperson;
        this.bcpos = bcpos;
        this.bcfield1 = bcfield1;

        this.bcfield2 = bcfield2;
        this.bcfield3 = bcfield3;
        this.bcoffiaddress = bcoffiaddress;
        this.bcphone1 = bcphone1;

        this.bcphone2 = bcphone2;
        this.bcemailaddress = bcemailaddress;
        this.bcWebsit = bcWebsit;
        this.bcdescriptn = bcdescriptn;

        this.bclogo = bclogo;
        this.bclongitude = bclongitude;
        this.bclatitude = bclatitude;
        this.bccategory = bccategory;
        this.bcprofilephoto = bcprofilephoto;
        this.bcregstatus = bcregstatus;
    }


    public String getBcbusinesname() {
        return bcbusinesname;
    }

    public String getbcperson() {
        return bcperson;
    }

    public String getBcpos() {
        return bcpos;
    }

    public String getBcfield1() {
        return bcfield1;
    }

    public String getBcfield2() {
        return bcfield2;
    }

    public String getBcfield3() {
        return bcfield3;
    }

    public String getBcoffiaddress() {
        return bcoffiaddress;
    }

    public String getBcphone1() {
        return bcphone1;
    }

    public String getBcphone2() {
        return bcphone2;
    }

    public String getBcEmailaddress() {
        return bcemailaddress;
    }

    public String getBcWebsite() {
        return bcWebsit;
    }

    public String getBcdescriptn() {return bcdescriptn;}

    public String getBclogo() {
        return bclogo;
    }

    public String getBcLongitude() {
        return bclongitude;
    }

    public String getBcLatitude() {
        return bclatitude;
    }


    public String getBcCategory() {
        return bccategory;
    }

    public String getBcprofilephoto() {
        return bcprofilephoto;
    }

    public String getBcregstatus() {
        return bcregstatus;
    }

}
