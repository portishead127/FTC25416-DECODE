package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;

@TeleOp(name = "TEST - Camera Test", group = "TEST")
public class CameraTest extends LinearOpMode {
    CameraSwivel cameraSwivel;
    @Override
    public void runOpMode() throws InterruptedException {
        cameraSwivel = new CameraSwivel(hardwareMap, telemetry, true, false);
        telemetry.addData("STATUS:", "INITIALISED");
        telemetry.addData("MOTIF:", cameraSwivel.motif.name());
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            cameraSwivel.update(true, gamepad1.left_stick_x);
            telemetry.addData("GAMEPAD 1 L_X:", gamepad1.left_stick_x);
            telemetry.update();
        }
    }
}
