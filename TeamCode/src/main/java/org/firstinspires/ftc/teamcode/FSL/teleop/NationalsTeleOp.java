package org.firstinspires.ftc.teamcode.FSL.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;

public class NationalsTeleOp extends OpMode {
    NationalsRobot robot;
    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
    }
    @Override
    public void loop() {
        robot.setIntakeRequested(gamepad2.right_trigger_pressed);

        if(gamepad2.triangleWasPressed()){robot.fireAny();}
        if(gamepad2.crossWasPressed()){robot.fireGreen();}
        if(gamepad2.squareWasPressed()){robot.firePurple();}
        if(gamepad2.circleWasPressed()){robot.fireAny();}

        if(gamepad2.dpadDownWasPressed()){robot.forceIntake();}

        robot.setDriveTrain(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.update();
    }
}
