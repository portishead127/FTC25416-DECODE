package org.firstinspires.ftc.teamcode.FSL.helper;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Turret;

public class Robot {

    public Camera camera;
    public Turret turret;
    public Shooter shooter;
    public Storage storage;
    public DriveTrain driveTrain;
    public Intake intake;
    public PedroFollowerDriveTrain pedroFollowerDriveTrain;

    public static double goalFieldX;
    public static double goalFieldY;

    public void determineGoalPos(boolean isBlue){
        if(isBlue){
            goalFieldX = 5;
        }
        else{
            goalFieldX = 135;
        }
        goalFieldY = 140;
    }

    // ===========================
    // Constructors
    // ===========================
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage) {
        camera = new Camera(hm, telemetry, isBlue);
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, emptyStorage);
        intake = new Intake(hm, telemetry);
        turret = new Turret(hm, telemetry);
        determineGoalPos(isBlue);
    }

    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage, Pose startingPose) {
        camera = new Camera(hm, telemetry, isBlue);
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, emptyStorage);
        intake = new Intake(hm, telemetry);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hm, telemetry, startingPose);
        determineGoalPos(isBlue);
    }

    // ===========================
    // Helper classes / methods
    // ===========================
    public static class ShotInfo {
        public double range;
        public double shotDirX;
        public double shotDirY;
        public double robotVelAlongShot;
    }
    private ShotInfo computeHybridTarget(Pose odomPose, Vector odomVel) {
        ShotInfo info = new ShotInfo();

        double dx;
        double dy;

        if (camera.locked) {

            // Camera gives robot-relative coordinates:
            // +x = right
            // +y = forward

            double relRight = camera.x;
            double relForward = camera.y;

            // Convert robot-relative to field-relative
            double heading = odomPose.getHeading();

            double forwardFieldX = Math.cos(heading);
            double forwardFieldY = Math.sin(heading);

            double leftFieldX = -Math.sin(heading);
            double leftFieldY = Math.cos(heading);

            // Convert right to left
            double relLeft = -relRight;

            dx = relForward * forwardFieldX + relLeft * leftFieldX;
            dy = relForward * forwardFieldY + relLeft * leftFieldY;

        } else {

            // Pure odometry fallback
            dx = goalFieldX - odomPose.getX();
            dy = goalFieldY - odomPose.getY();
        }

        info.range = Math.hypot(dx, dy);

        if (info.range > 1e-6) {
            info.shotDirX = dx / info.range;
            info.shotDirY = dy / info.range;
        } else {
            info.shotDirX = 0;
            info.shotDirY = 0;
        }

        // Project velocity along shot
        info.robotVelAlongShot =
                odomVel.getXComponent() * info.shotDirX +
                        odomVel.getYComponent() * info.shotDirY;
        return info;
    }
    private void calculateAndSetTurretPIDTarget(ShotInfo info, Pose odomPose) {
        double fieldAngle = Math.atan2(info.shotDirY, info.shotDirX);
        double robotAngle = fieldAngle - odomPose.getHeading();

        while(robotAngle > Math.PI){robotAngle -= 2 * Math.PI;}
        while(robotAngle < -Math.PI){robotAngle += 2 * Math.PI;}

        turret.setPIDTarget(
                TurretConfig.TICKS_PER_RADIAN * robotAngle,
                false
        );
    }
    private void updateSubsystems(boolean queueEmpty, double range, double robotVelAlongShot) {
        shooter.dynamicUpdate(queueEmpty, range, robotVelAlongShot);
        storage.update(shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        turret.update();
    }
    private void handleQueueButtons(Gamepad gamepad) {
        if (gamepad.squareWasPressed()) storage.setQueue(Scoring.convertToScoringPattern(camera.motif));
        if (gamepad.triangleWasPressed()) storage.setQueue(Scoring.G);
        if (gamepad.crossWasPressed()) storage.setQueue(Scoring.P);
        if (gamepad.circleWasPressed()) storage.setQueue(Scoring.NONE);
    }
    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        Pose odomPose = pedroFollowerDriveTrain.follower.getPose();
        Vector odomVel = pedroFollowerDriveTrain.follower.getVelocity();

        ShotInfo shot = computeHybridTarget(odomPose, odomVel);
        calculateAndSetTurretPIDTarget(shot, odomPose);
        updateSubsystems(storage.queueIsEmpty(), shot.range, shot.robotVelAlongShot);
        pedroFollowerDriveTrain.update(gamepad1);
        handleQueueButtons(gamepad2);
    }
    public void autoUpdate(Follower follower) {
        Pose odomPose = follower.getPose();
        Vector odomVel = follower.getVelocity();

        ShotInfo shot = computeHybridTarget(odomPose, odomVel);
        calculateAndSetTurretPIDTarget(shot, odomPose);

        updateSubsystems(storage.queueIsEmpty(), shot.range, shot.robotVelAlongShot);
    }
}
