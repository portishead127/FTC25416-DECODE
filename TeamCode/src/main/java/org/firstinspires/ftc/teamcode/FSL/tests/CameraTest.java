package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Camera;

@TeleOp(name = "TEST - Camera Test", group = "TEST")
public class CameraTest extends OpMode {
    Camera camera;
    @Override
    public void init() {
        camera = new Camera(hardwareMap, telemetry, true);
        telemetry.addData("STATUS:", "INITIALISED");
        telemetry.addData("MOTIF:", camera.motif.name());
        camera.resumeStreaming();
        telemetry.update();
    }

    @Override
    public void loop() {
        camera.focusOnAprilTag();
        camera.sendTelemetry();
        telemetry.update();
    }
}
