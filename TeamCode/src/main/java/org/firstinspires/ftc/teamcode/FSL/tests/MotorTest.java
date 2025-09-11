package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Test: Motor Test", group = "Test")
public class MotorTest extends LinearOpMode
{
    DcMotorEx testMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        testMotor = hardwareMap.get(DcMotorEx.class, "test");
        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            if(gamepad1.square) currentPower = 0;

            if(currentPower > 1.0) currentPower = 1.0;
            if(currentPower < -1.0) currentPower = -1.0;

            testMotor.setPower(currentPower);

            telemetry.addData("RADIANS?", testMotor.getVelocity(AngleUnit.RADIANS));
            telemetry.addData("RPM", testMotor.getVelocity(AngleUnit.RADIANS) * 60/ (2 * Math.PI));
            telemetry.addData("CURRENT MOTOR POWER: ", currentPower);
            telemetry.addData("STORED MOTOR POWER: ", storedPower);

            telemetry.update();

            sleep(50);


        }

    }
}
