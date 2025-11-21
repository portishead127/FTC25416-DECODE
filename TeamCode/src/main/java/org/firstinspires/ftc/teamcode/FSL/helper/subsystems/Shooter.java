package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Shooter{
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
        motor1.setVelocity(2 * Math.PI);
        motor2.setVelocity(2 * Math.PI);
    }
    public void fireHalf(){
        motor1.setVelocity(Math.PI);
        motor2.setVelocity(Math.PI);
    };
    public void stop() {
        motor1.setVelocity(0);
        motor2.setVelocity(0);
    }
    public void addToServo(){ servo.setPosition(servo.getPosition() + 0.01); }
    public void substractFromServo(){ servo.setPosition(servo.getPosition() - 0.01); }
    public void telemetryServo(){ telemetry.addData("SHOOTER SERVO", servo.getPosition()); }
}