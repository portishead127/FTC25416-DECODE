package org.firstinspires.ftc.teamcode.FSL.helper.colors;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorMethods {
    public static Colors fromSensor(ColorSensor sensor) {
        float[] hsv = new float[3];

        double scalingFactor = 255.0f / sensor.alpha();
        int red = (int)((double)sensor.red() * scalingFactor);
        int green = (int)((double)sensor.green() * scalingFactor);
        int blue = (int)((double)sensor.blue() * scalingFactor);

        Color.RGBToHSV(
                red,
                green,
                blue,
                hsv
        );
        return fromHsv(hsv);
    }

    private static Colors fromHsv(float[] hsv) {
        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        if(sat < 0.3 || val < 0.2) {
            return Colors.NONE;
        }

        if ((hue >= 0 && hue < 20) || (hue >= 320 && hue < 360)) { //THIS IS RED
            return Colors.GREEN;
        } else if (hue >= 140 && hue <= 200) { //THIS IS BLUE
            return Colors.PURPLE;
        } else {
            return Colors.NONE;
        }
    }
}
