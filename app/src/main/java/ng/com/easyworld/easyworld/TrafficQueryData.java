package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 7/28/2018.
 */

public class TrafficQueryData {

    private String msgId;
    private String Location;
    private String VeryFree;
    private String MovingSlowly;
    private String StandStill;




    TrafficQueryData(String msgId,String Location,String VeryFree,String MovingSlowly,String StandStill)
    {
        this.Location = Location;
        this.VeryFree = VeryFree;
        this.MovingSlowly = MovingSlowly;
        this.StandStill = StandStill;
        this.msgId = msgId;
    }

    public void setMsgId(String val){
        msgId = val;
    }
    public void setLocation(String val){
        Location = val;
    }
    public void setVeryFree(String val){
        VeryFree = val;
    }
    public void setMovingSlowly(String val){
        MovingSlowly = val;
    }
    public void setStandStill(String  val){
        StandStill = val;
    }

    public String getMsgId(){
        return msgId;
    }
    public String getLocation(){
        return Location;
    }
    public String getVeryFree(){
        return VeryFree;
    }
    public String getMovingSlowly(){
        return MovingSlowly;
    }
    public String getStandStill(){
        return StandStill;
    }

}
