package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name= "TEST: Shooter and Camera Test", group = "TEST")
public class ShooterCameraTest extends OpMode {

    Camera camera;
    Shooter shooter;
    @Override
    public void init() {
        camera = new Camera(hardwareMap, telemetry, true, false);
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        camera.update(gamepad1.left_stick_x);
        shooter.simpleUpdate(gamepad1.right_bumper, Math.sqrt(camera.x * camera.x + camera.y * camera.y));
        telemetry.update();
    }
}
