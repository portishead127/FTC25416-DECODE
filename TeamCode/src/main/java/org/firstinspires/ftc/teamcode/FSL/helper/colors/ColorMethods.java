package org.firstinspires.ftc.teamcode.FSL.helper.colors;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorMethods {
    public static Color fromSensor(ColorSensor sensor) {
        float[] hsv = new float[3];

        double scalingFactor = 255.0f / sensor.alpha();
        int red = sensor.red();
        int green = sensor.green();
        int blue = sensor.blue();

        android.graphics.Color.RGBToHSV(
                red,
                green,
                blue,
                hsv
        );
        return fromHsv(hsv);
    }

    private static Color fromHsv(float[] hsv) {
        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        if(sat < 0.45 || val < 0.2) {
            return Color.NONE;
        }

        if ((hue >= 150 && hue < 170)) {
            return Color.GREEN;
        } else if (hue >= 220 && hue <= 240) {
            return Color.PURPLE;
        } else {
            return Color.NONE;
        }
    }
}
