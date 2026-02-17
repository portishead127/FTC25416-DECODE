package org.firstinspires.ftc.teamcode.FSL.comp;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;

//FTC DASH - 192.168.49.1:8080/dash
//PANELS - 192.168.49.1:8001/
@TeleOp(name = "BLUE: Competition", group = "COMP")
public class CompetitionTeleOpBlue extends OpMode {
    public static Pose startingPose;
    Robot robot;
    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, true, false, startingPose);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }
    @Override
    public void loop() {
        robot.update(gamepad1, gamepad2);
        telemetry.update();
    }
}
