package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;

@TeleOp(name = "TEST - Camera Test", group = "TEST")
public class CameraTest extends LinearOpMode {
    CameraSwivel cameraSwivel;
    @Override
    public void runOpMode() throws InterruptedException {
        cameraSwivel = new CameraSwivel(hardwareMap, telemetry, true);
        telemetry.addData("STATUS:", "INITIALISED");
        waitForStart();
        while(opModeIsActive()){
            cameraSwivel.focusOnAprilTag(true);
        }
    }
}
