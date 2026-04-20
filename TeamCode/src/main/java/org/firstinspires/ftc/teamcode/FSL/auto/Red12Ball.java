package org.firstinspires.ftc.teamcode.FSL.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;

public class Red12Ball extends OpMode {
    NationalsRobot robot;
    Paths paths;
    int pathState;
    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;
        public PathChain Path7;
        public PathChain Path8;
        public PathChain Path9;
        public PathChain Path10;
        public PathChain Path11;
        public PathChain Path12;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(124.000, 123.000),

                                    new Pose(96.185, 95.073)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(215), Math.toRadians(50))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.185, 95.073),

                                    new Pose(103.606, 59.730)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.606, 59.730),

                                    new Pose(133.436, 59.293)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(133.436, 59.293),
                                    new Pose(97.118, 67.934),
                                    new Pose(128.459, 70.120)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))

                    .build();

            Path5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(128.459, 70.120),
                                    new Pose(74.224, 61.714),
                                    new Pose(96.375, 95.135)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(50))

                    .build();

            Path6 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.375, 95.135),

                                    new Pose(103.954, 84.054)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path7 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.954, 84.054),

                                    new Pose(127.641, 83.965)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path8 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(127.641, 83.965),

                                    new Pose(96.185, 94.981)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(50))

                    .build();

            Path9 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.185, 94.981),

                                    new Pose(103.494, 35.317)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path10 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.494, 35.317),

                                    new Pose(133.992, 35.027)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path11 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(133.992, 35.027),

                                    new Pose(96.297, 94.865)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(50))

                    .build();

            Path12 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.297, 94.865),

                                    new Pose(96.946, 72.811)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();
        }
    }

    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
        paths = new Paths(robot.getFollower());
        pathState = 0;
    }

    @Override
    public void loop() {
        robot.autoUpdate();
        doAuto();
    }

    private void doAuto(){
        switch(pathState){
            case 0:
                robot.followPath(paths.Path1, true);
                pathState = 1;
                break;
            case 1:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.LAYUP);
                    pathState = 2;
                }
                break;
            case 2:
                if(robot.finishedShooting()){
                    robot.followPath(paths.Path2, true);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.Path3, true, 0.4);
                    pathState = 4;
                }
                break;
            case 4:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.Path4, true);
                    pathState = 5;
                }
                break;
            case 5:
                if(!robot.isFollowerBusy()){
                    robot.followPath(paths.Path5, true);
                    pathState = 6;
                }
                break;
            case 6:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.LAYUP);
                    pathState = 7;
                }
                break;
            case 7:
                if(robot.finishedShooting()){
                    robot.followPath(paths.Path6, true);
                    pathState = 8;
                }
                break;
            case 8:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.Path7, true, 0.4);
                    pathState = 9;
                }
                break;
            case 9:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.Path8, true);
                    pathState = 10;
                }
                break;
            case 10:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.LAYUP);
                    pathState = 11;
                }
                break;
            case 11:
                if(robot.finishedShooting()){
                    robot.followPath(paths.Path9, true);
                    pathState = 12;
                }
                break;
            case 12:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.Path10, true, 0.4);
                    pathState = 13;
                }
                break;
            case 13:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.Path11, true);
                    pathState = 14;
                }
                break;
            case 14:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.LAYUP);
                    pathState = 15;
                }
                break;
            case 15:
                if(robot.finishedShooting()){
                    robot.followPath(paths.Path12, true);
                    pathState = 16;
                }
                break;
            case 16:
                break;
        }
    }
}
