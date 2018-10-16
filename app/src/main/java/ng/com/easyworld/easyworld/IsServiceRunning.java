package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Oisrael on 7/1/2018.
 */

public class IsServiceRunning {

    private Activity activity;

    IsServiceRunning(Activity activity){
        this.activity = activity;
    }


    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
