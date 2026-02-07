package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;

@TeleOp(name = "TEST: Intake Test", group = "TEST")
public class IntakeTest extends OpMode {
    Intake intake;
    @Override
    public void init() {
        intake = new Intake(hardwareMap,telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        intake.update(gamepad1.right_bumper);
        telemetry.update();
    }
}
