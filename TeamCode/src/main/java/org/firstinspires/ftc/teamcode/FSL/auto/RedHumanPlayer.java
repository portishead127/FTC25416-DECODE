package org.firstinspires.ftc.teamcode.FSL.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;
import org.firstinspires.ftc.teamcode.FSL.teleop.NationalsTeleOp;

@Autonomous(name = "RED: Human Player (Far)", group = "RED")
public class RedHumanPlayer extends OpMode {
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
                                    new Pose(81.000, 9.000),

                                    new Pose(84.232, 84.510)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(45))

                    .build();

            GetBottomRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(84.232, 84.510),
                                    new Pose(76.477, 30.591),
                                    new Pose(131.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Shoot = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(131.000, 36.000),
                                    new Pose(83.153, 27.815),
                                    new Pose(83.954, 83.676)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))

                    .build();

            HumanPlayerIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(83.954, 83.676),
                                    new Pose(72.755, 6.585),
                                    new Pose(131.000, 9.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            ThreePointer = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(131.000, 9.000),

                                    new Pose(86.456, 13.436)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(65))

                    .build();

            Escape = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(86.456, 13.436),

                                    new Pose(85.386, 36.089)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(65), Math.toRadians(90))

                    .build();
        }
    }

    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry,false, false);
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
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.TIP_OF_TRIANGLE);
                    pathState = 2;
                }
                break;
            case 2:
                if(robot.finishedShooting()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.GetBottomRow, true, 0.4);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.Shoot, true);
                    pathState = 4;
                }
                break;
            case 4:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.TIP_OF_TRIANGLE);
                    pathState = 5;
                }
                break;
            case 5:
                if(robot.finishedShooting()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.HumanPlayerIntake, true, 0.4);
                    pathState = 6;
                }
                break;
            case 6:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.ThreePointer, true);
                    pathState = 7;
                }
                break;
            case 7:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.TIP_OF_TRIANGLE);
                    pathState = 8;
                }
                break;
            case 8:
                if(robot.finishedShooting()){
                    robot.followPath(paths.Escape, true);
                    pathState = 9;
                }
                break;
            case 9:
                if(!robot.isFollowerBusy()){
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