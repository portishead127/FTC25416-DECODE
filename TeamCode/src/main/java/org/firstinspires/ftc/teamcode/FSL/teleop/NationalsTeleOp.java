package org.firstinspires.ftc.teamcode.FSL.teleop;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.helper.control.Localization;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.GoalPose;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;

@TeleOp(name = "COMP")
public class NationalsTeleOp extends OpMode {
    public static Pose startingPose;
    NationalsRobot robot;
    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
        robot.setStartingPose(startingPose);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        selectArenaProperties();
    }

    @Override
    public void loop() {
        if(gamepad2.right_trigger_pressed) robot.SIMPLEintake();
        else if (gamepad2.rightTriggerWasReleased()) {robot.SIMPLEintakestop();}
        if(gamepad2.triangleWasPressed()){robot.SIMPLEprepareForShot(ShotPos.LAYUP);}
        if(gamepad2.squareWasPressed()){robot.SIMPLEprepareForShot(ShotPos.TIP_OF_TRIANGLE);}
        if(gamepad2.crossWasPressed()){robot.SIMPLEprepareForShot(ShotPos.THREE_POINTER);}
        if(gamepad2.dpad_up) robot.setBlockerOpen();

        selectArenaProperties();

        robot.setDriveTrain(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_trigger_pressed, gamepad1.left_trigger_pressed);
        if(gamepad1.triangle) robot.faceGoal();
        robot.update();
        telemetry.addData("triangle", gamepad1.triangle);
        telemetry.addData("X", robot.getPose().getX());
        telemetry.addData("Y", robot.getPose().getY());
        telemetry.addData("Heading", robot.getPose().getHeading());
        telemetry.update();
    }

    private void selectArenaProperties(){
        if(gamepad1.dpadLeftWasPressed()) Localization.setDesiredGoalPose(GoalPose.blueGoal);
        if(gamepad1.dpadRightWasPressed()) Localization.setDesiredGoalPose(GoalPose.redGoal);
    }
}
