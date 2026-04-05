package org.firstinspires.ftc.teamcode.FSL.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.teleop.NationalsTeleOp;

@Autonomous(name = "RED: Goal Start", group = "RED")
public class RedGoalStart extends OpMode {
    NationalsRobot robot;
    Paths paths;
    int pathState;
    public static class Paths {
        public PathChain TipOfTriangle;
        public PathChain IntakeTopRow;
        public PathChain TipOfTriangle2;
        public PathChain IntakeMiddleRow;
        public PathChain TipOfTriangle3;
        public PathChain Escape;

        public Paths(Follower follower) {
            TipOfTriangle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(124.000, 123.000),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(215), Math.toRadians(90))

                    .build();

            IntakeTopRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(73.313, 85.506),
                                    new Pose(128.340, 84.278)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            TipOfTriangle2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(128.340, 84.278),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))

                    .build();

            IntakeMiddleRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(72.000, 60.000),
                                    new Pose(131.954, 59.444)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            TipOfTriangle3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(131.954, 59.444),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))

                    .build();

            Escape = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(72.000, 72.000),

                                    new Pose(88.834, 121.066)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(90))

                    .build();
        }
    }

    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry,false, true);
        paths = new Paths(robot.getFollower());
        robot.setStartingPose(paths.TipOfTriangle.firstPath().getFirstControlPoint().withHeading(Math.toRadians(-45)));
        pathState = 0;
    }
    @Override
    public void loop() {
        doAuto();
        robot.update();
    }
    public void doAuto(){
        switch(pathState){
            case 0:
                robot.followPath(paths.TipOfTriangle, true);
                pathState = 1;
                break;
            case 1:
                robot.readMotif();
                robot.fireMotif();
                pathState = 2;
                break;
            case 2:
                if(robot.isIntaking()){
                    robot.setIntakeRequested(true);
                    robot.followPath(paths.IntakeTopRow, true);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
                    robot.setIntakeRequested(false);
                    robot.followPath(paths.TipOfTriangle2, true);
                    pathState = 4;
                }
                break;
            case 4:
                if(!robot.isFollowerBusy()){
                    robot.fireMotif();
                    pathState = 5;
                }
                break;
            case 5:
                if(robot.isIntaking()){
                    robot.setIntakeRequested(true);
                    robot.followPath(paths.IntakeMiddleRow, true);
                    pathState = 6;
                }
                break;
            case 6:
                if(!robot.isFollowerBusy()){
                    robot.setIntakeRequested(false);
                    robot.followPath(paths.TipOfTriangle3, true);
                    pathState = 7;
                }
                break;
            case 7:
                if(!robot.isFollowerBusy()){
                    robot.fireMotif();
                    pathState = 8;
                }
                break;
            case 8:
                if(robot.isIntaking()){
                    robot.followPath(paths.Escape, true);
                    pathState = 9;
                }
                break;
            case 9:
                if(!robot.isFollowerBusy()){
                    robot.toggleFaceForward();
                    pathState = -1;
                }
                break;
        }
    }
    @Override
    public void stop() {
        NationalsTeleOp.startingPose = robot.getPose();
    }
}