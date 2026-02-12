package org.firstinspires.ftc.teamcode.FSL.comp;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;

@TeleOp(name = "RED: Competition", group = "RED")
public class CompetitionTeleOpRed extends OpMode {
    Robot robot;
    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false, false, false, new Pose(82.000, 60.000));
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }
    @Override
    public void loop() {
        robot.simpleUpdate(gamepad1, gamepad2);
        telemetry.update();
    }
}
