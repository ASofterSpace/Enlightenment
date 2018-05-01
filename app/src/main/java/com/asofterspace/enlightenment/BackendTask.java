package com.asofterspace.enlightenment;

import android.widget.TextView;

import com.asofterspace.toolbox.web.WebAccessor;

import java.io.IOException;

/**
 * Created by Moya on 01.05.2018.
 */

public class BackendTask {

    private WebAccessor webAccessor;

    private BackendTarget target;

    private int r;
    private int g;
    private int b;

    public BackendTask(BackendTarget target, int r, int g, int b) {

        this.webAccessor = new WebAccessor();

        this.target = target;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void execute() {

        switch (target) {
            case WZ:
                executeWZ();
                break;
            case BW:
                executeBW();
                break;
            default:
                executeWZ();
                executeBW();
                break;
        }
    }

    private void executeWZ() {

        // actually turn off the lamp
        if ((r < 1) && (g < 1) && (b < 1)) {

            // TODO :: do not hardcode the IP address, but instead discover it!
            // TODO :: do not hardcode an authorized user, but instead generate an authorization on the device and store it!
            webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state", "{\"on\":false}");

        } else {

        /*
        int M = Math.max(Math.max(r, g), b);
        int m = Math.min(Math.min(r, g), b);
        int C = M - m;
        */


            webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state", "{\"on\":true, \"sat\":254, \"bri\":254,\"hue\":49000}");
        }
    }

    private void executeBW() {

        webAccessor.get("http://192.168.178.44:5000/colors/" + colorToHex(r, g, b));
    }

    private String colorToHex(int r, int g, int b) {

        StringBuilder result = new StringBuilder();

        result.append(Integer.toHexString(r));
        while (result.length() < 2) {
            result.insert(0, "0");
        }

        result.append(Integer.toHexString(g));
        while (result.length() < 4) {
            result.insert(0, "0");
        }

        result.append(Integer.toHexString(b));
        while (result.length() < 6) {
            result.insert(0, "0");
        }

        return result.toString();
    }
}
