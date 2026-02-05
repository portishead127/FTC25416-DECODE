
package org.firstinspires.ftc.teamcode.FSL.comp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

@Autonomous(name = "BLUE - Greedy Auto", group = "Autonomous")
@Configurable // Panels
public class BlueGreedyAuto extends OpMode {
    private Robot robot;
    private Motif motif;
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, true);

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(20, 123, Math.toRadians(-35)));

        paths = new Paths(follower); // Build paths
        setPathState(0);

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        autonomousPathUpdate(); // Update autonomous state machine
        if(motif != null){
            robot.update();
        };

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain TipOfTriangle;
        public PathChain HitLever;
        public PathChain InfrontOfHighRow;
        public PathChain IntakeTopRow;
        public PathChain TipOfTriangle2;
        public PathChain IntakeMiddleRow;
        public PathChain TipOfTriangle3;
        public PathChain IntakeLowRow;
        public PathChain ThreePointer;
        public PathChain Escape;

        public Paths(Follower follower) {
            TipOfTriangle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(20.000, 123.000),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(135))

                    .build();

            HitLever = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(72.000, 72.000),

                                    new Pose(16.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))

                    .build();

            InfrontOfHighRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(16.000, 72.000),
                                    new Pose(72.000, 72.000),
                                    new Pose(48.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            IntakeTopRow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(48.000, 84.000),

                                    new Pose(24.000, 84.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            TipOfTriangle2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(24.000, 84.000),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            IntakeMiddleRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(72.000, 60.000),
                                    new Pose(24.000, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))

                    .build();

            TipOfTriangle3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(24.000, 60.000),

                                    new Pose(72.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))

                    .build();

            IntakeLowRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(72.000, 72.000),
                                    new Pose(72.000, 36.000),
                                    new Pose(24.000, 36.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            ThreePointer = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(24.000, 36.000),

                                    new Pose(56.000, 8.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(90))

                    .build();

            Escape = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.000, 8.000),

                                    new Pose(56.000, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))

                    .build();
        }
    }


    public void autonomousPathUpdate(){
        switch (pathState) {
            case 0:
                follower.followPath(paths.TipOfTriangle);
                setPathState(1);

                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    motif = robot.cameraSwivel.readMotif();
                    robot.storage.setQueue(Scoring.convertToScoringPattern(motif));
                    if(robot.storage.queueIsEmpty()) {
                        follower.followPath(paths.HitLever, true);
                        setPathState(2);
                    }
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(paths.InfrontOfHighRow,true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */
                    robot.intake.run();
                    follower.followPath(paths.IntakeTopRow, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    robot.intake.stop();
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(paths.TipOfTriangle2,true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */
                    robot.storage.setQueue(Scoring.convertToScoringPattern(motif));
                    if(robot.storage.queueIsEmpty()){
                        robot.intake.run();
                        follower.followPath(paths.IntakeMiddleRow,true);
                        setPathState(6);
                    }
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    robot.intake.stop();
                    follower.followPath(paths.TipOfTriangle3, true);
                    setPathState(7);
                }
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    robot.storage.setQueue(Scoring.convertToScoringPattern(motif));
                    if(robot.storage.queueIsEmpty()){
                        robot.intake.run();
                        follower.followPath(paths.IntakeLowRow, true);
                        setPathState(8);
                    }
                }
                break;
            case 8:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    robot.intake.stop();
                    follower.followPath(paths.ThreePointer, true);
                    setPathState(9);
                }
                break;
            case 9:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    robot.storage.setQueue(Scoring.convertToScoringPattern(motif));
                    if(robot.storage.queueIsEmpty()){
                        follower.followPath(paths.Escape, true);
                        setPathState(10);
                    }
                }
                break;
            case 10:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}
    