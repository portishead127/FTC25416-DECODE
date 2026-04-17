package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;
@Disabled
@TeleOp(name = "TEST: Servo Position Test", group = "TEST")
public class ServoStoragePositionTest extends OpMode {
    ServoStorage storage;
    double servoPos;
    double servoStep;

    @Override
    public void init() {
        storage = new ServoStorage(hardwareMap, telemetry);
        storage.setServos(0);
        servoPos = 0;
        servoStep = 0.05;
    }

    @Override
    public void loop() {
        if(gamepad1.dpadRightWasPressed()) servoStep += 0.01;
        if(gamepad1.dpadLeftWasPressed()) servoStep -= 0.01;
        if(gamepad1.dpadUpWasPressed()) servoPos += servoStep;
        if(gamepad1.dpadDownWasPressed()) servoPos -= servoStep;
        if(gamepad1.dpadDownWasPressed()) servoPos -= servoStep;

        if(gamepad1.triangleWasPressed()) storage.setServos(servoPos);

        telemetry.addData("Step", servoStep);
        telemetry.addData("Selected pos", servoPos);
        telemetry.addData("Actual pos", storage.getServoPos());
        telemetry.addData("Encoder reading", storage.getEncoderPos());
        telemetry.update();
    }
}
