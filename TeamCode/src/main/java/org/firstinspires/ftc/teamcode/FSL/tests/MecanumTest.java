package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;

@TeleOp(name = "TEST: Mecanum Test", group = "TEST")
public class MecanumTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumSet mecanumSet = new MecanumSet(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            mecanumSet.drive(gamepad1, gamepad1.right_bumper);
        }
    }
}
