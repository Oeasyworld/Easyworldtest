package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 5/22/2018.
 */

public class TrafficPrivateMessage {

    private String type;
    private String trafficAnswer;
    private double minLat;
    private double maxLat;
    private double minLongi;
    private double maxLongi;
    private String from;
    private String to;
    private String fromConn;
    private String toConn;


    //Setters
    public void setTypee(String val){
        type = val;
    }
    public void setTrafficAnswer(String val){
        trafficAnswer = val;
    }
    public void setMinLat(double val){
        minLat = val;
    }
    public void setMaxLat(double val){
        maxLat = val;
    }
    public void setMinLongi(double val){
        minLongi = val;
    }
    public void setMaxLongi(double val){
        maxLongi = val;
    }
    public void setFrom(String val){
        from = val;
    }
    public void setTo(String val){
        to = val;
    }
    public void setFromConn(String val){
        fromConn = val;
    }
    public void setToConn(String val){
        toConn = val;
    }



    //Getters
    public String getTypee(){
        return type;
    }
    public String getTrafficAnswer(){
        return trafficAnswer;
    }
    public double getMinLat(){
        return minLat;
    }
    public double getMaxLat(){
        return maxLat;
    }
    public double getMinLongi(){
        return minLongi;
    }
    public double getMaxLongi(){
        return maxLongi;
    }
    public String getFrom(){
        return from;
    }
    public String getTo(){
        return to;
    }
    public String getToConn(){
        return toConn;
    }
    public String getFromConn(){
        return fromConn;
    }


}
