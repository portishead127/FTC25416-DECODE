package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Shooter{
    private static final double MAX_MOTOR_RPM = 933;
    private final DcMotorEx motor1;
    private final DcMotorEx motor2;
    private final Servo servo;
    private final Telemetry telemetry;
    public Shooter(HardwareMap hm, Telemetry telemetry){
        motor1 = hm.get(DcMotorEx.class, "SM1");
        motor2 = hm.get(DcMotorEx.class, "SM2");
        servo = hm.get(Servo.class, "SHS");
        this.telemetry = telemetry;
    }
    public void fire(){
        motor1.setVelocity(motor1.getMotorType().getAchieveableMaxTicksPerSecond());
        motor2.setVelocity(motor1.getMotorType().getAchieveableMaxTicksPerSecond());
    }
    public void fireHalf(){
        motor1.setVelocity(motor1.getMotorType().getAchieveableMaxTicksPerSecond()/2);
        motor2.setVelocity(motor2.getMotorType().getAchieveableMaxTicksPerSecond()/2);
    };
    public void stop() {
        motor1.setVelocity(0);
        motor2.setVelocity(0);
    }
    public void addToServo(){ servo.setPosition(servo.getPosition() + 0.01); }
    public void subtractFromServo(){ servo.setPosition(servo.getPosition() - 0.01); }
    public void sendTelemetry(){
        telemetry.addLine("SHOOTER\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR 1 VEL", motor1.getVelocity());
        telemetry.addData("MOTOR 2 VEL", motor2.getVelocity());
    }
}