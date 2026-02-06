package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.ShooterConfig;

public class Shooter {
    private static double MOTOR1_MAX_TICKS_PER_SECOND;
    private final DcMotorEx motor1;
    private final Servo servo;
    private final Telemetry telemetry;
    private double motorScalar;

    public Shooter(HardwareMap hm, Telemetry telemetry) {
        motor1 = hm.get(DcMotorEx.class, "SM1");
        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);
        this.telemetry = telemetry;
        MOTOR1_MAX_TICKS_PER_SECOND = motor1.getMotorType().getAchieveableMaxTicksPerSecond();
    }

    public void update(boolean queueEmpty, double range){
        boolean in3Pointer = range > ShooterConfig.LIMITFOR3POINTERRANGE;
        if(in3Pointer){
            servo.setPosition(ShooterConfig.SERVOPOSFOR3POINTER);
        }
        else{
            servo.setPosition(ShooterConfig.SERVOPOSFORLAYUP);
        }

        if(!queueEmpty){
            if(in3Pointer){
                motorScalar = ShooterConfig.MOTORVELSCALARFOR3POINTER;
            }
            else{
                motorScalar = ShooterConfig.MOTORVELSCALARFORLAYUP;
            }
        }
        else{
            motorScalar = 0;
        }
        motor1.setVelocity(MOTOR1_MAX_TICKS_PER_SECOND * motorScalar);
    }
    public boolean isWarmedUp() {
        double currentVelocity = motor1.getVelocity();  // ticks per second (actual measured)
        double target = MOTOR1_MAX_TICKS_PER_SECOND * motorScalar;

        // True if within 90% or better, with a small epsilon to stabilize near boundary
        return currentVelocity >= (target * ShooterConfig.WARM_UP_THRESHOLD) - ShooterConfig.VELOCITY_EPSILON;
    }

    public void fire(double scalar) {
        motor1.setVelocity(scalar * MOTOR1_MAX_TICKS_PER_SECOND);
    }
    public void stop() {
        motor1.setVelocity(0);
    }
    public void addToServo() {
        servo.setPosition(servo.getPosition() + 0.1);
    }
    public void subtractFromServo() {
        servo.setPosition(servo.getPosition() - 0.05);
    }
    public void sendTelemetry() {
        telemetry.addLine("SHOOTER\n------------------------------");
        telemetry.addData("Servo POS", String.format("%.3f", servo.getPosition()));
        telemetry.addData("Motor Vel (actual)", String.format("%.0f tps", motor1.getVelocity()));
        telemetry.addData("Target Vel", String.format("%.0f tps", MOTOR1_MAX_TICKS_PER_SECOND));
        telemetry.addData("Warmed Up", isWarmedUp() ? "YES" : "NO");
        telemetry.addData("Vel % of Target", String.format("%.1f%%", (motor1.getVelocity() / MOTOR1_MAX_TICKS_PER_SECOND) * 100));
    }
}