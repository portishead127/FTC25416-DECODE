package org.firstinspires.ftc.teamcode.FSL.comp;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;

@TeleOp(name = "RED: Competition", group = "COMP")
public class CompetitionTeleOpRed extends OpMode {
    public static Pose startingPose;
    public static Motif motif;
    Robot robot;
    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false, false, startingPose);
        robot.camera.motif = motif;
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.addData("MOTIF", motif.name());
        telemetry.update();
    }
    @Override
    public void loop() {
        robot.update(gamepad1, gamepad2);
        telemetry.update();
    }
}
