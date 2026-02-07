package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;

@TeleOp(name = "TEST: Intake and Mecanum Test", group = "TEST")
public class IntakeMecanumTest extends OpMode {

    Intake intake;
    DriveTrain driveTrain;
    @Override
    public void init() {
        intake = new Intake(hardwareMap,telemetry);
        driveTrain = new DriveTrain(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop() {
        intake.update(gamepad1.square);
        driveTrain.update(gamepad1, gamepad1.right_bumper);
        telemetry.update();
    }
}
