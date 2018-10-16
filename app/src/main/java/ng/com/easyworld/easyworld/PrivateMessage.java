package ng.com.easyworld.easyworld;

import android.os.Parcel;
import android.os.Parcelable;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.io.Serializable;

/**
 * Created by Oisrael on 1/16/2018.
 */

public class PrivateMessage implements Serializable{





    private String type;
    private SessionDescription offer;
    private String name;
    private SessionDescription answer;
    private String msg;
    private IceCandidate candidate;
    private String success;
    private String connectionid;
    private String From;
    private String To;
    private String fromConn;
    private String tim;
    private String toConn;
    private String p;
    private String msgId;
    private String openV;
    private String trafficQuestion;
    private String trafficAnswer;
    private double minLat;
    private double maxLat;
    private double minLongi;
    private double maxLongi;




    //Setters
    public void setTypee(String val){
        type = val;
    }
    public void setOffer(SessionDescription val){
        offer = val;
    }
    public void setNamee(String val){
        name = val;
    }
    public void setAnswer(SessionDescription val){
        answer = val;
    }
    public void setCandidate(IceCandidate val){
        candidate = val;
    }
    public void setSuccess(String val){
        success = val;
    }
    public void setConnectionid(String val){
        connectionid = val;
    }
    public void setFromm(String val){
        From = val;
    }
    public void setToo(String val){
        To = val;
    }
    public void setFromConn(String val){
        fromConn = val;
    }
    public void setToConn(String val){
        toConn = val;
    }
    public void setMsg(String val){
        msg = val;
    }
    public void setMsgId(String val){
        msgId = val;
    }
    public void setPw(String val){
        p = val;
    }
    public void setOpenV(String val){
        openV = val;
    }
    public void setTim(String val){
        tim = val;
    }


    public void setTrafficQuestion(String val){
        trafficQuestion = val;
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



    //Getters
    public String getTypee(){
        return type;
    }
    public SessionDescription getOffer(){
        return offer;
    }
    public String getNamee(){
        return name;
    }
    public SessionDescription getAnswer(){
        return answer;
    }
    public String getMsg(){
        return msg;
    }
    public String getMsgId(){
        return msgId;
    }
    public IceCandidate getCandidate(){
        return candidate;
    }
    public String getSuccess(){
        return success;
    }
    public String getConnectionid(){
        return connectionid;
    }
    public String getFromm(){
        return From;
    }
    public String getToo(){
        return To;
    }
    public String getFromConn(){
        return type;
    }
    public String getToConn(){
        return toConn;
    }
    public String getPw(){
        return p;
    }
    public String getOpenV(){
        return openV;
    }

    public String getTrafficQuestion(){
        return trafficQuestion;
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
    public String getTim(){
        return tim;
    }



}
