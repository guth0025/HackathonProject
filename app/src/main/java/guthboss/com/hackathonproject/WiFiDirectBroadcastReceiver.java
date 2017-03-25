package guthboss.com.hackathonproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

import java.util.HashSet;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private HashSet<Device> devices;
    private WifiP2pManager mManager;
    private Channel mChannel;
    private MainActivity mActivity;
    private IntentFilter mIntentFilter;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;

        devices = new HashSet<>( 50);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void discoverPeers() {
        mManager.discoverPeers( mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                mActivity.clearChat();
                devices.clear();
                mActivity.addTextToChat( "Looking for Peers");
            }

            @Override
            public void onFailure(int reason){
                mActivity.addTextToChat( "Failed peer discovery: " + reason);
            }
        });
    }

    public IntentFilter getIntenets(){
        return mIntentFilter;
    }

    public void connectTo( String address){
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = address;
        mManager.connect( mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                mActivity.addTextToChat( "connected to a6:e4:b8:ab:3b:25");
                new AnnounceServerAsyncTask().execute( "Hellooooooooooooooooooo");
            }

            @Override
            public void onFailure(int reason) {
                mActivity.addTextToChat( "Failed connect: " + reason);
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            mActivity.addTextToChat( "STATE_CHANGED_ACTION");
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                mActivity.addTextToChat( "WIFI direct available");
            } else {
                mActivity.addTextToChat( "WIFI direct NOT available");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            mActivity.addTextToChat( "PEERS_CHANGED_ACTION");
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            mManager.requestPeers(mChannel,  new PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    Device device;
                    for( WifiP2pDevice d : wifiP2pDeviceList.getDeviceList()){
                        device = new Device(d);
                        if( devices.add( device)){
                            mActivity.addTextToChat( device.toString());
                        }
                    }
                }
            });
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            mActivity.addTextToChat( "CONNECTION_CHANGED_ACTION");
            // Respond to new connection or disconnections
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if( networkInfo.isConnected()){
                mActivity.addTextToChat( "Still Connected to: " + networkInfo);
            }else{
                mActivity.addTextToChat( "Connection lost: " + networkInfo.getReason());
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            mActivity.addTextToChat( "DEVICE_CHANGED_ACTION");
            // Respond to this device's wifi state changing
            mActivity.addTextToChat(intent.getParcelableExtra( WifiP2pManager.EXTRA_WIFI_P2P_DEVICE).toString());
        }
    }
}