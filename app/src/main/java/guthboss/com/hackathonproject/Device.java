package guthboss.com.hackathonproject;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by Shariar on 3/24/2017.
 */

public class Device {

    private String name, address;

    public Device( WifiP2pDevice device){
        name = device.deviceName;
        address = device.deviceAddress;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (!name.equals(device.name)) return false;
        return address.equals(device.address);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + "\n" + address;
    }
}
