package ng.com.easyworld.easyworld;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;


/**
 * A simple {@link Fragment} subclass.
 */
public class FChat extends Fragment {


    public FChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fchat, container, false);



    }

}
