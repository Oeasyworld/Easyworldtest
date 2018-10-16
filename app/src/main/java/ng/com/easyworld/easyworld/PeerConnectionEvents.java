package ng.com.easyworld.easyworld;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;

/**
 * Peer connection events.
 */
public interface PeerConnectionEvents {
    /**
     * Callback fired once local SDP is created and set.
     */
    public void onLocalDescription(final SessionDescription sdp);

    /**
     * Callback fired once local Ice candidate is generated.
     */
    public void onIceCandidate(final IceCandidate candidate);

    /**
     * Callback fired once connection is established (IceConnectionState is
     * CONNECTED).
     */
    public void onIceConnected();

    /**
     * Callback fired once connection is closed (IceConnectionState is
     * DISCONNECTED).
     */
    public void onIceDisconnected();

    /**
     * Callback fired once peer connection is closed.
     */
    public void onPeerConnectionClosed();

    /**
     * Callback fired once peer connection statistics is ready.
     */
    public void onPeerConnectionStatsReady(final StatsReport[] reports);

    /**
     * Callback fired once peer connection error happened.
     */
    public void onPeerConnectionError(final String description);
}
