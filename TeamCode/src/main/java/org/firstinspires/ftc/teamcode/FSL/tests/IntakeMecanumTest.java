package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.configs.MecanumConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;

@TeleOp(name = "Test: Intake and Mecanum Test", group = "Test")
public class IntakeMecanumTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Intake intake = new Intake(hardwareMap,telemetry);
        MecanumSet mecanumSet = new MecanumSet(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            intake.run(gamepad1.right_bumper);
            mecanumSet.drive(gamepad1, MecanumConfig.MECANUM_POWER);
        }
    }
}
