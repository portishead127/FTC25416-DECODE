package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;

@TeleOp(name = "TEST: Mecanum Test", group = "TEST")
public class MecanumTest extends OpMode {
    DriveTrain driveTrain;
    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }
    @Override
    public void loop(){
        driveTrain.setFast(gamepad1.left_trigger_pressed && !gamepad1.right_trigger_pressed);
        driveTrain.setSlow(gamepad1.right_trigger_pressed && !gamepad1.left_trigger_pressed);
        driveTrain.setMed(!gamepad1.right_trigger_pressed && !gamepad1.left_trigger_pressed);
        driveTrain.setDriveCoefficients(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        driveTrain.update();
        driveTrain.sendTelemetry();
        telemetry.update();
    }
}
