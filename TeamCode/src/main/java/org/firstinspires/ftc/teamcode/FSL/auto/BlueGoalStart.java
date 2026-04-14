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
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;
import org.firstinspires.ftc.teamcode.FSL.teleop.NationalsTeleOp;

@Autonomous(name = "BLUE: Goal Start", group = "BLUE")
public class BlueGoalStart extends OpMode {
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
                                    new Pose(25.000, 120.000),

                                    new Pose(57.266, 83.954)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(135))

                    .build();

            IntakeTopRow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.266, 83.954),

                                    new Pose(15.722, 83.444)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            TipOfTriangle2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(15.722, 83.444),

                                    new Pose(56.988, 83.954)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            IntakeMiddleRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(56.988, 83.954),
                                    new Pose(72.000, 60.000),
                                    new Pose(15.000, 59.444)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            TipOfTriangle3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(15.000, 59.444),
                                    new Pose(65.707, 60.081),
                                    new Pose(57.266, 84.232)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            Escape = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.266, 84.232),

                                    new Pose(55.166, 121.066)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(90))

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
        robot.autoUpdate();
    }
    public void doAuto(){
        switch(pathState){
            case 0:
                robot.followPath(paths.TipOfTriangle, true);
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
                    robot.followPath(paths.IntakeTopRow, true, 0.4);
                    pathState = 3;
                }
                break;
            case 3:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.TipOfTriangle2, true);
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
                    robot.followPath(paths.IntakeMiddleRow, true, 0.4);
                    pathState = 6;
                }
                break;
            case 6:
                if(!robot.isFollowerBusy()){
                    robot.SIMPLEintakestop();
                    robot.followPath(paths.TipOfTriangle3, true);
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