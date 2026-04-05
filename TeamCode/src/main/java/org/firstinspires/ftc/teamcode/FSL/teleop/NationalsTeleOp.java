package org.firstinspires.ftc.teamcode.FSL.teleop;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.helper.control.Localization;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.GoalPose;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;

public class NationalsTeleOp extends OpMode {
    public static Pose startingPose;
    NationalsRobot robot;
    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
        robot.setStartingPose(startingPose);
    }

    @Override
    public void init_loop() {
        selectArenaProperties();
    }

    @Override
    public void loop() {
        robot.setIntakeRequested(gamepad2.right_trigger_pressed);

        if(gamepad2.triangleWasPressed()){robot.fireMotif();}
        if(gamepad2.crossWasPressed()){robot.fireGreen();}
        if(gamepad2.squareWasPressed()){robot.firePurple();}
        if(gamepad2.circleWasPressed()){robot.fireAny();}

        if(gamepad2.dpadDownWasPressed()){robot.forceIntake();}

        selectArenaProperties();

        robot.setDriveTrain(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_trigger_pressed, gamepad1.left_trigger_pressed);
        robot.update();
    }

    private void selectArenaProperties(){
        if(gamepad1.dpadLeftWasPressed()) robot.setMotif(Motif.GPP);
        if(gamepad1.dpadUpWasPressed()) robot.setMotif(Motif.PGP);
        if(gamepad1.dpadRightWasPressed()) robot.setMotif(Motif.PPG);

        if(gamepad2.dpadLeftWasPressed()) Localization.setDesiredGoalPose(GoalPose.blueGoal);
        if(gamepad2.dpadRightWasPressed()) Localization.setDesiredGoalPose(GoalPose.redGoal);
    }
}
