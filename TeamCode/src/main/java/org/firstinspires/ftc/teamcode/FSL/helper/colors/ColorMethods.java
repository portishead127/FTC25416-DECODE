package org.firstinspires.ftc.teamcode.FSL.helper.colors;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.jetbrains.annotations.TestOnly;

public class ColorMethods {
    public static Color fromSensor(ColorRangeSensor sensor) {
        if(sensor.getDistance(DistanceUnit.MM) > 10){
            return Color.NONE;
        }
        else{
            float[] hsv = new float[3];

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
    }

    @TestOnly
    public static Color fromHsv(float[] hsv) {
        float hue = hsv[0];
        if ((hue >= 150 && hue < 170)) {
            return Color.GREEN;
        } else if (hue >= 220 && hue <= 240) {
            return Color.PURPLE;
        } else {
            return Color.NONE;
        }
    }
}
