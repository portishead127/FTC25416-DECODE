package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.Camera;

@TeleOp(name = "TEST - Camera Test", group = "TEST")
public class CameraTest extends OpMode {
    Camera camera;
    @Override
    public void init() {
        camera = new Camera();
        telemetry.addData("STATUS:", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop() {
        camera.readMotif();
        telemetry.update();
    }
}
