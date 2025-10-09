package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
public class ServoTest extends LinearOpMode {
    Servo servo;
    @Override
    public void runOpMode() throws InterruptedException {
        servo= hardwareMap.get(Servo.class,"SPS");
        waitForStart();
        while(opModeIsActive()){
            servo.setPosition(0);
        }
    }
}
