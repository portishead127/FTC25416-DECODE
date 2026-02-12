package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name= "TEST: Storage and Shooter Test", group = "TEST")
public class ShooterCameraTest extends OpMode {

    CameraSwivel cameraSwivel;
    Shooter shooter;
    @Override
    public void init() {
        cameraSwivel = new CameraSwivel(hardwareMap, telemetry, true, false);
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        cameraSwivel.update(gamepad1.left_stick_x);
        shooter.simpleUpdate(gamepad1.right_bumper, Math.sqrt(cameraSwivel.x * cameraSwivel.x + cameraSwivel.y * cameraSwivel.y));
        telemetry.update();
    }
}
