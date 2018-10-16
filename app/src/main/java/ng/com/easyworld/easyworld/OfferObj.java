package ng.com.easyworld.easyworld;

import org.webrtc.SessionDescription;

/**
 * Created by Oisrael on 9/16/2018.
 */

public class OfferObj {

  private static SessionDescription offer;
  private static String from;
  OfferObj(SessionDescription offer,String from){
      this.offer = offer;
      this.from = from;
  }
    OfferObj(){}
  public SessionDescription getOffer(){
      return offer;
  }
    public String getFrom(){
        return from;
    }
}
