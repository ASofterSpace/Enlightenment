package com.asofterspace.enlightenment;

import android.widget.TextView;

import com.asofterspace.toolbox.web.WebAccessor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Moya on 01.05.2018.
 */

public class BackendTask {

    private WebAccessor webAccessor;

    private Set<BackendTarget> targets;

    private String specops = null;

    private int r;
    private int g;
    private int b;

    private int roundCounter = 0;

    public BackendTask(BackendTarget target, int r, int g, int b) {

        this.webAccessor = new WebAccessor();

        this.targets = new HashSet<>();
        this.targets.add(target);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public BackendTask(Set<BackendTarget> targets, int r, int g, int b) {

        this.webAccessor = new WebAccessor();

        this.targets = new HashSet<>();
        this.targets.addAll(targets);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public BackendTask(Set<BackendTarget> targets, String specops) {

        this.webAccessor = new WebAccessor();

        this.targets = new HashSet<>();
        this.targets.addAll(targets);

        this.specops = specops;
    }

    // this is called upon the first execution of the task
    public void execute() {

        roundCounter = 0;

        if (targets.contains(BackendTarget.WZ) || targets.contains(BackendTarget.ALL)) {
            executeWZ();
        }

        if (targets.contains(BackendTarget.BW) || targets.contains(BackendTarget.ALL)) {
            executeBW();
        }
    }

    // this is called 100 ms later (and again... and again... and again...)
    public void executeAgain() {

        roundCounter++;

        if (targets.contains(BackendTarget.WZ) || targets.contains(BackendTarget.ALL)) {
            executeAgainWZ();
        }

        if (targets.contains(BackendTarget.BW) || targets.contains(BackendTarget.ALL)) {
            executeAgainBW();
        }
    }

    private void executeWZ() {

        if ("rainbow".equals(specops)) {
            r = 255;
            g = 0;
            b = 128;
        }

        if ("twinkle".equals(specops)) {
            r = 255;
            g = 255;
            b = 255;
        }

        if ("strobored".equals(specops)) {
            r = 255;
            g = 0;
            b = 0;
        }

        if ("pulsred".equals(specops)) {
            r = 255;
            g = 0;
            b = 0;
        }

        // actually turn off the lamp
        if ((r < 1) && (g < 1) && (b < 1)) {

            // TODO :: do not hardcode the IP address, but instead discover it!
            // TODO :: do not hardcode an authorized user, but instead generate an authorization on the device and store it!
            webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state",
                    "{\"on\":false}");

            return;
        }

        setWZColor();
    }

    private void executeAgainWZ() {

        boolean colorChanged = false;

        if ("rainbow".equals(specops)) {
            // TODO .. actually do this nicer, maybe?
            r = Math.abs(((roundCounter * 2) % 512) - 256);
            g = Math.abs((((roundCounter * 2) + 128) % 512) - 256);
            b = Math.abs((((roundCounter * 2) - 128) % 512) - 256);
            colorChanged = true;
        }

        if ("twinkle".equals(specops)) {
            // get 0..512, back to 0..512...
            // move down to -256..256
            // get abs, so 256..0..256, back from start
            r = Math.abs(((roundCounter * 4) % 512) - 256);
            g = r;
            b = r;
            colorChanged = true;
        }

        if ("strobored".equals(specops)) {
            if (roundCounter % 3 == 0) {
                if (roundCounter % 2 == 0) {
                    r = 255;
                    g = 0;
                    b = 0;
                    colorChanged = true;
                } else {
                    r = 0;
                    g = 0;
                    b = 0;
                    colorChanged = true;
                }
            }
        }

        if ("pulsred".equals(specops)) {
            // get 0..512, back to 0..512...
            // move down to -256..256
            // get abs, so 256..0..256, back from start
            r = Math.abs(((roundCounter * 10) % 512) - 256);
            g = 0;
            b = 0;
            colorChanged = true;
        }

        if (colorChanged) {
            setWZColor();
        }
    }

    private void setWZColor() {
        // see https://en.wikipedia.org/wiki/HSL_and_HSV
        // the Hue light is using HSV (hue, saturation and value, which is brightness)

        // initial definitions
        int M = Math.max(Math.max(r, g), b);
        int m = Math.min(Math.min(r, g), b);
        int C = M - m;

        // intermediate calculations
        float H = 0;
        if (C != 0) {
            if (M == r) {
                H = ((g - b) / C) % 6;
            }
            if (M == g) {
                H = ((b - r) / C) + 2;
            }
            if (M == b) {
                H = ((r - g) / C) + 4;
            }
        }
        int V = M;

        // final values of interest
        int saturation = 0;
        if (V != 0) {
            saturation = (255 * C) / V;
        }
        int brightness = V;
        int hue = Math.round(65535 * H) / 6;
        if (hue < 0) {
            hue += 65536;
        }

        webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state",
                "{\"on\":true, \"sat\":" + saturation + ", \"bri\":" + brightness + ",\"hue\":" + hue + "}");
    }

    private void executeBW() {

        if (specops != null) {
            webAccessor.get("http://192.168.178.44:5000/colors/" + specops);
        } else {
            webAccessor.get("http://192.168.178.44:5000/colors/" + colorToHex(r, g, b));
        }
    }

    private void executeAgainBW() {

    }

    private String colorToHex(int r, int g, int b) {

        StringBuilder result = new StringBuilder();

        result.append(Integer.toHexString(r));
        while (result.length() < 2) {
            result.insert(0, "0");
        }

        result.append(Integer.toHexString(g));
        while (result.length() < 4) {
            result.insert(2, "0");
        }

        result.append(Integer.toHexString(b));
        while (result.length() < 6) {
            result.insert(4, "0");
        }

        return result.toString();
    }
}
