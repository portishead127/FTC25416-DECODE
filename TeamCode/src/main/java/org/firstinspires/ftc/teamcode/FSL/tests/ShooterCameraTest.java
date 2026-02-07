package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name= "TEST: Storage and Shooter Test", group = "TEST")
public class ShooterCameraTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CameraSwivel cameraSwivel = new CameraSwivel(hardwareMap, telemetry, true, false);
        Shooter shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            cameraSwivel.update(true, gamepad1.left_stick_x);
            shooter.update(gamepad1.right_bumper, cameraSwivel.range);
            telemetry.update();
        }
    }
}
