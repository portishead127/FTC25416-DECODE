package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsShooter;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Shooter;

@TeleOp(name = "BLOCKER")
public class BlockerTest extends OpMode {
    double pos = 0.5;
    NationalsShooter shooter;
    @Override
    public void init() {
        shooter = new NationalsShooter(hardwareMap, telemetry);
    }
    @Override
    public void loop() {
        if(gamepad1.dpadUpWasPressed()) pos += 0.05;
        if(gamepad1.dpadDownWasPressed()) pos -= 0.05;
        if(gamepad1.triangleWasPressed()) shooter.setBlocker(pos);

        telemetry.addData("pos", pos);
        telemetry.update();
    }
}
