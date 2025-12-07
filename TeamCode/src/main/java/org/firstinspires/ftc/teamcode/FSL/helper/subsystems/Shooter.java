package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Shooter{
    private static double MOTOR1_MAX_TICKS_PER_SECOND;
    private final DcMotorEx motor1;
    private final Servo servo;
    private final Telemetry telemetry;
    public Shooter(HardwareMap hm, Telemetry telemetry){
        motor1 = hm.get(DcMotorEx.class, "SM1");
        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);
        this.telemetry = telemetry;
        MOTOR1_MAX_TICKS_PER_SECOND = motor1.getMotorType().getAchieveableMaxTicksPerSecond();
    }
    public void fire(){
        motor1.setVelocity(MOTOR1_MAX_TICKS_PER_SECOND);
    }
    public void fireHalf(){
        motor1.setVelocity(0.5 * MOTOR1_MAX_TICKS_PER_SECOND);
    };
    public void stop() {
        motor1.setVelocity(0);
    }
    public void addToServo(){ servo.setPosition(servo.getPosition() + 0.1); }
    public void subtractFromServo(){ servo.setPosition(servo.getPosition() - 0.05); }
    public void sendTelemetry(){
        telemetry.addLine("SHOOTER\n--------------`----------------------");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR 1 VEL", motor1.getVelocity());
    }
}