package org.firstinspires.ftc.teamcode.FSL.auto;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;
import org.firstinspires.ftc.teamcode.FSL.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;
import org.firstinspires.ftc.teamcode.FSL.teleop.NationalsTeleOp;

@Autonomous(name = "BLUE: Collect From Gate - CO-OP (Far)", group = "BLUE")
public class BlueCollectionCoop extends OpMode {
    NationalsRobot robot;
    Paths paths;
    ElapsedTime intakeWaitTimer;
    int pathState;
    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;
        public PathChain Path7;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(63.000, 9.000),

                                    new Pose(59.095, 19.838)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(118))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(59.095, 19.838),
                                    new Pose(51.289, 35.118),
                                    new Pose(38.362, 34.948)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(38.362, 34.948),

                                    new Pose(8.867, 34.683)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(8.867, 34.683),
                                    new Pose(26.912, 12.766),
                                    new Pose(58.928, 19.796)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(118))

                    .build();

            Path5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(58.928, 19.796),
                                    new Pose(7.908, 8.760),
                                    new Pose(9.269, 48.219)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(118), Math.toRadians(100))

                    .build();

            Path6 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(9.269, 48.219),

                                    new Pose(58.979, 19.861)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(100), Math.toRadians(118))

                    .build();

            Path7 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(58.979, 19.861),

                                    new Pose(58.498, 35.633)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(90))

                    .build();
        }
    }
    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
        paths = new Paths(robot.getFollower());
        robot.setStartingPose(paths.Path1.firstPath().getFirstControlPoint().withHeading(Math.toRadians(90)));
        pathState = 0;
        intakeWaitTimer = new ElapsedTime();
    }

    @Override
    public void loop() {
        doAuto();
        robot.autoUpdate();
    }

    private void doAuto(){
        switch (pathState){
            case 0:
                robot.followPath(paths.Path1, true);
                pathState = 1;
                break;
            case 1:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.THREE_POINTER);
                    pathState = 2;
                }
                break;
            case 2:
                if(robot.finishedShooting()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.Path2, true);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
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
            case 5:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.THREE_POINTER);
                    pathState = 6;
                }
                break;
            case 6:
                if(robot.finishedShooting()){
                    robot.SIMPLEintake();
                    robot.followPath(paths.Path5, true, 0.5);
                    pathState = 7;
                }
                break;
            case 7:
                if(!robot.isFollowerBusy()){
                    if(intakeWaitTimer.milliseconds() >= 3000){
                        robot.SIMPLEintakestop();
                        robot.followPath(paths.Path6, true);
                        pathState = 8;
                    }
                }
                else intakeWaitTimer.reset();
                break;
            case 8:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEprepareForShot(ShotPos.THREE_POINTER);
                    pathState = 9;
                }
                break;
            case 9:
                if(robot.finishedShooting()){
                    robot.SIMPLEstopShooter();
                    robot.followPath(paths.Path7, true);
                    pathState = 10;
                }
                break;
        }
    }
    @Override
    public void stop() {
        NationalsTeleOp.startingPose = robot.getPose();
    }
}
