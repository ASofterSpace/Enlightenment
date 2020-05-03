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

    // TODO :: do not hardcode the IP address, but instead discover it using https://www.meethue.com/api/nupnp
    private final String HUE_IP = "192.168.178.80";
    // TODO :: do not hardcode an authorized user, but instead generate an authorization on the device and store it!
    private final String HUE_AUTH = "OsYo-UTn1Bz--jK6tDhMSQQWSw0o-Q49xffRheJa";
    private final String HUE_API = "http://" + HUE_IP + "/api/" + HUE_AUTH + "/";

    // TODO :: do not hardcode the IP address, but instead get it from the user or some configuration!
    private final String BOULDERWALL_IP = "192.168.178.21";
    private final String BOULDERWALL_API = "http://" + BOULDERWALL_IP + ":5000/colors/";
    private final String WOHNZIMMER_IP = "192.168.178.57";
    private final String WOHNZIMMER_API = "http://" + WOHNZIMMER_IP + ":5000/colors/";

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

        if (targets.contains(BackendTarget.H1) || targets.contains(BackendTarget.H2) ||
            targets.contains(BackendTarget.H3) || targets.contains(BackendTarget.H4) ||
            targets.contains(BackendTarget.H5) || targets.contains(BackendTarget.BENE)) {
            executeHue();
        }

        if (targets.contains(BackendTarget.BW) || targets.contains(BackendTarget.WZ)) {
            executeLED();
        }
    }

    // this is called 100 ms later (and again... and again... and again...)
    public void executeAgain() {

        roundCounter++;

        if (targets.contains(BackendTarget.H1) || targets.contains(BackendTarget.H2) ||
            targets.contains(BackendTarget.H3) || targets.contains(BackendTarget.H4) ||
            targets.contains(BackendTarget.H5) || targets.contains(BackendTarget.BENE)) {
            executeAgainHue();
        }

        if (targets.contains(BackendTarget.BW) || targets.contains(BackendTarget.WZ)) {
            executeAgainLED();
        }
    }

    private void executeHue() {

        if ("off".equals(specops)) {
            r = 0;
            g = 0;
            b = 0;
        }

        if ("dim".equals(specops)) {
            r = 52;
            g = 52;
            b = 52;
        }

        if ("medium".equals(specops)) {
            r = 152;
            g = 152;
            b = 152;
        }

        if ("max".equals(specops)) {
            r = 255;
            g = 255;
            b = 255;
        }

        if ((specops != null) && specops.startsWith("brightness:")) {
            int bri = Integer.parseInt(specops.substring(11));
            r = bri;
            g = bri;
            b = bri;
        }

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

        if ("sunrise".equals(specops)) {
            r = 1;
            g = 1;
            b = 1;
        }

        // actually turn off the lamp
        if ((r < 1) && (g < 1) && (b < 1)) {

            instructHue("{\"on\":false}");

            return;
        }

        setHueColor();
    }

    private void executeAgainHue() {

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

        // slow down: only show every 10th update
        if (roundCounter % 10 == 1) {
            if ("sunrise".equals(specops)) {
                r++;
                g++;
                b++;

                colorChanged = true;

                if (r > 255) {
                    r = 255;
                    colorChanged = false;
                }
                if (g > 255) {
                    g = 255;
                    colorChanged = false;
                }
                if (b > 255) {
                    b = 255;
                    colorChanged = false;
                }
            }
        }

        if (colorChanged) {
            setHueColor();
        }
    }

    private void setHueColor() {

        /*
        // this is our old code, trying to convert to HSV... however, the bulbs also understand
        // CIE x,y, which is much easier to convert to from RGB, so let's use that instead! :)
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

        instructHue("{\"on\":true, \"sat\":" + saturation + ", \"bri\":" + brightness + ",\"hue\":" + hue + "}");
        */

        double x1 = 0.4124*r + 0.3576*g + 0.1805*b;
        double y1 = 0.2126*r + 0.7152*g + 0.0722*b;
        double z1 = 0.0193*r + 0.1192*g + 0.9505*b;

        double sum = x1 + y1 + z1;

        double x = x1 / sum;
        double y = y1 / sum;

        if (specops == null) {
            // do not set brightness when the color was set via RGB
            instructHue("{\"on\":true, \"xy\": [" + x + ", " + y + "]}");
        } else {
            // do set brightness when the color was set via specops
            int brightness = (int) Math.round((x1 + y1 + z1) / 3);
            if (brightness > 254) {
                brightness = 254;
            }

            instructHue("{\"on\":true, \"xy\": [" + x + ", " + y + "], \"bri\":" + brightness + "}");
        }
    }

    private void instructHue(String instruction) {

        if (targets.contains(BackendTarget.H1)) {
            webAccessor.put(HUE_API + "lights/1/state", instruction);
        }

        if (targets.contains(BackendTarget.H2)) {
            webAccessor.put(HUE_API + "lights/2/state", instruction);
        }

        if (targets.contains(BackendTarget.H3)) {
            webAccessor.put(HUE_API + "lights/3/state", instruction);
        }

        if (targets.contains(BackendTarget.H4)) {
            webAccessor.put(HUE_API + "lights/4/state", instruction);
        }

        if (targets.contains(BackendTarget.H5)) {
            webAccessor.put(HUE_API + "lights/5/state", instruction);
        }

        if (targets.contains(BackendTarget.BENE)) {
            webAccessor.put(HUE_API + "lights/6/state", instruction);
        }
    }

    private void executeLED() {

        if ("off".equals(specops)) {
            r = 0;
            g = 0;
            b = 0;
            specops = null;
        }

        if ("dim".equals(specops)) {
            r = 105;
            g = 74;
            b = 25;
            specops = null;
        }

        if ("medium".equals(specops)) {
            r = 185;
            g = 138;
            b = 64;
            specops = null;
        }

        if ("max".equals(specops)) {
            r = 255;
            g = 255;
            b = 255;
            specops = null;
        }

        if ((specops != null) && specops.startsWith("brightness:")) {
            int bri = Integer.parseInt(specops.substring(11));
            r = bri;
            g = bri;
            b = bri;
            specops = null;
        }

        if (targets.contains(BackendTarget.BW)) {
            if (specops != null) {
                webAccessor.get(BOULDERWALL_API + specops);
            } else {
                webAccessor.get(BOULDERWALL_API + colorToHex(r, g, b));
            }
        }
        if (targets.contains(BackendTarget.WZ)) {
            if (specops != null) {
                webAccessor.get(WOHNZIMMER_API + specops);
            } else {
                webAccessor.get(WOHNZIMMER_API + colorToHex(r, g, b));
            }
        }
    }

    private void executeAgainLED() {

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
