package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;

import java.util.LinkedList;

public class BetterStorage {
    private final LinkedList<Color> queue = new LinkedList<Color>();
    private Color currentColor = Color.NONE;
    private final ColorSensor colorSensor;
    private final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    public boolean anyColor;
    private boolean isFlicking = false;
    private long flickStartTime = 0;

    private static final long FLICK_FORWARD_TIME = 200; // ms
    private static final long FLICK_RETURN_TIME = 350;  // ms total

    public BetterStorage(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SPM");
        servo = hm.get(Servo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);
        anyColor = false;
        this.telemetry = telemetry;
    }
    public void stop(){ motor.setVelocity(0);}
    public void spin(){ motor.setVelocity(motor.getMotorType().getAchieveableMaxTicksPerSecond() / 2); }
    public void setQueue(LinkedList<Color> colors){
        queue.clear();
        queue.addAll(colors);
    }
    public boolean queueIsEmpty(){ return queue.isEmpty(); }
    private void updateFlick() {
        if (!isFlicking) return;

        long elapsed = System.currentTimeMillis() - flickStartTime;

        if (elapsed < FLICK_FORWARD_TIME) {
            servo.setPosition(1); // flick forward
        }
        else if (elapsed < FLICK_RETURN_TIME) {
            servo.setPosition(0); // return
        }
        else {
            isFlicking = false;   // flick finished
        }
    }
    private void startFlick() {
        if (isFlicking) return; // prevent double flicks
        isFlicking = true;
        flickStartTime = System.currentTimeMillis();
    }

    public void loadIntoShooter() {
        updateFlick();

        if (isFlicking) {
            stop();
            return;
        }

        spin();
        currentColor = ColorMethods.fromSensor(colorSensor);

        if (anyColor && currentColor != Color.NONE) {
            stop();
            startFlick();
            return;
        }

        if (!queue.isEmpty() && currentColor == queue.peek()) {
            stop();
            startFlick();
            queue.remove();
        }
    }


    public void toggleAnyColor() {
        anyColor = !anyColor;
        setQueue(Scoring.NONE);
    }

    public final void flickBall(){
        servo.setPosition(1);
    }

    public final void unflickBall(){
        servo.setPosition(0);
    }
    public void sendTelemetry() {
        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(
                colorSensor.red(),
                colorSensor.green(),
                colorSensor.blue(),
                hsv
        );

        telemetry.addLine("STORAGE\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("COLOR SENSOR", currentColor.name());
        telemetry.addData("COLOR SENSOR R", colorSensor.red());
        telemetry.addData("COLOR SENSOR G", colorSensor.green());
        telemetry.addData("COLOR SENSOR B", colorSensor.blue());
        telemetry.addData("COLOR SENSOR HUE", hsv[0]);
    }
}
