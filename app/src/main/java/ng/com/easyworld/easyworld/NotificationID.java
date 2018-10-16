package ng.com.easyworld.easyworld;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Oisrael on 8/15/2018.
 */

public class NotificationID {

        private final static AtomicInteger c = new AtomicInteger(0);
        public  int getID() {
            return c.incrementAndGet();
        }

}
