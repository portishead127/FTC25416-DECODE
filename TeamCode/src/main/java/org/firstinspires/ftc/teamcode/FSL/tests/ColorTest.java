package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;

@TeleOp(name = "TEST - Color Test", group = "TEST")
public class ColorTest extends OpMode {
    ColorSensor colorSensor;

    @Override
    public void init() {
        colorSensor = hardwareMap.get(ColorSensor.class, "CS");
        telemetry.addData("STATUS:", "INITIALISED");
        telemetry.update();
    }


    @Override
    public void loop() {
        float[] hsv = new float[3];

        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();

        android.graphics.Color.RGBToHSV(
                red,
                green,
                blue,
                hsv
        );

        Color color = ColorMethods.fromSensor(colorSensor);
        telemetry.addData("RED", red);
        telemetry.addData("GREEN", green);
        telemetry.addData("BLUE", blue);
        telemetry.addData("HUE", hsv[0]);
        telemetry.addData("SAT", hsv[1]);
        telemetry.addData("VAL", hsv[2]);
        telemetry.addData("COLOR", color.name());
        telemetry.update();
    }
}