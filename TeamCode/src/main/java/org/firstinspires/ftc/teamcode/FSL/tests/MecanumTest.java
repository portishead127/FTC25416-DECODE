package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;

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
        driveTrain.update(gamepad1, gamepad1.right_bumper);
        telemetry.update();
    }
}
