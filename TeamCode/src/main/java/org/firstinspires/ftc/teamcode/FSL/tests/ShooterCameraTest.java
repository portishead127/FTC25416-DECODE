package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Shooter;

@Disabled
@TeleOp(name= "TEST: Shooter and Camera Test", group = "TEST")
public class ShooterCameraTest extends OpMode {

    Camera camera;
    Shooter shooter;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        telemetry.update();
    }
}
