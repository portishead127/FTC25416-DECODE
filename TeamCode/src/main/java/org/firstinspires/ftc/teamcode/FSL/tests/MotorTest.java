package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Test: Motor Test", group = "Test")
public class MotorTest extends LinearOpMode
{
    DcMotor testMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        testMotor = hardwareMap.get(DcMotorEx.class, "test");

        waitForStart();

        double currentPower = 0.0;
        double storedPower = 0.0;

        while(opModeIsActive()){

            if(gamepad1.dpad_up) currentPower += 0.1;
            if(gamepad1.dpad_down) currentPower -= 0.1;
            if(gamepad1.dpad_left) currentPower += 0.05;
            if(gamepad1.dpad_right) currentPower -= 0.05;
            if(gamepad1.circle) storedPower = currentPower;
            if(gamepad1.triangle) currentPower = storedPower;

            testMotor.setPower(currentPower);
        }

    }
}
