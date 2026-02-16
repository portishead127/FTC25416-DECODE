package org.firstinspires.ftc.teamcode.FSL.helper.colors;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorMethods {
    public static Color fromSensor(ColorRangeSensor sensor) {
        if(sensor.getDistance(DistanceUnit.MM) > 50){
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
