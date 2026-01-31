package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;

import java.util.LinkedList;

public class Storage {
    private final LinkedList<Color> queue = new LinkedList<Color>();
    private Color currentColor = Color.NONE;
    private final ColorSensor colorSensor;
    private final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    public Storage(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SPM");
        servo = hm.get(Servo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);

        this.telemetry = telemetry;
    }
    public void stop(){ motor.setVelocity(0);}
    public void spin(){ motor.setVelocity(motor.getMotorType().getAchieveableMaxTicksPerSecond() / 2); }
    public void setQueue(LinkedList<Color> colors){
        queue.clear();
        queue.addAll(colors);
    }
    public boolean queueIsEmpty(){ return queue.isEmpty(); }
    public void loadIntoShooter() {
        spin();
        unflickBall();

        currentColor = ColorMethods.fromSensor(colorSensor);
        if (currentColor == queue.peek()) {
            stop();
            flickBall();
            queue.remove();
        }
    }
    public final void flickBall(){
        servo.setPosition(1);
    }

    public final void unflickBall(){
        servo.setPosition(0);
    }
    public void sendTelemetry() {
        telemetry.addLine("STORAGE\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("COLOR SENSOR", currentColor.name());
    }
}
