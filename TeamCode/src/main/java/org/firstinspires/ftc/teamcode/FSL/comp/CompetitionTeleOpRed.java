package org.firstinspires.ftc.teamcode.FSL.comp;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;

@TeleOp(name = "RED: Competition", group = "COMP")
public class CompetitionTeleOpRed extends OpMode {
    public static Pose startingPose;
    Robot robot;
    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false, false, startingPose);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }
    @Override
    public void loop() {
        robot.update(gamepad1, gamepad2);
        telemetry.update();
    }
}
