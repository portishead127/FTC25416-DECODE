package org.firstinspires.ftc.teamcode.FSL.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.Robot;
import org.firstinspires.ftc.teamcode.FSL.teleop.NationalsTeleOp;

@Autonomous(name = "BLUE: Far Start", group = "BLUE")
public class BlueFarStart extends OpMode {
    NationalsRobot robot;
    Paths paths;
    int pathState;
    public static class Paths {
        public PathChain GetMotifShoot;
        public PathChain GetBottomRow;
        public PathChain Shoot;
        public PathChain HumanPlayerIntake;
        public PathChain ThreePointer;
        public PathChain Escape;
        public Paths(Follower follower) {
            GetMotifShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(63.000, 9.000),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))

                    .build();

            GetBottomRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(84.481, 33.649),
                                    new Pose(9.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            Shoot = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(9.000, 36.000),
                                    new Pose(65.017, 37.266),
                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            HumanPlayerIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(71.245, 6.585),
                                    new Pose(9.000, 9.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            ThreePointer = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(9.000, 9.000),

                                    new Pose(62.826, 14.548)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            Escape = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(62.826, 14.548),

                                    new Pose(58.614, 36.089)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(90))

                    .build();
        }
    }

    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry,false, true);
        paths = new Paths(robot.getFollower());
        robot.setStartingPose(paths.GetMotifShoot.firstPath().getFirstControlPoint().withHeading(Math.toRadians(90)));
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
                robot.followPath(paths.GetMotifShoot, true);
                pathState = 1;
                break;
            case 1:
                robot.readMotif();
                if(!robot.isFollowerBusy()){
                    robot.fireMotif();
                    pathState = 2;
                }
                break;
            case 2:
                if(robot.isIntaking()){
                    robot.setIntakeRequested(true);
                    robot.followPath(paths.GetBottomRow, true);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
                    robot.setIntakeRequested(false);
                    robot.followPath(paths.Shoot, true);
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
                    robot.followPath(paths.HumanPlayerIntake, true);
                    pathState = 6;
                }
                break;
            case 6:
                if(!robot.isFollowerBusy()){
                    robot.setIntakeRequested(false);
                    robot.followPath(paths.ThreePointer, true);
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