package org.firstinspires.ftc.teamcode.FSL.helper;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraDetectionConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

public class Robot {

    public CameraSwivel cameraSwivel;
    public Shooter shooter;
    public PIDStorage storage;
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
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isGreedyAuto, boolean emptyStorage) {
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, isGreedyAuto);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, isBlue, emptyStorage);
        intake = new Intake(hm, telemetry);
        determineGoalPos(isBlue);
    }

    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage, Pose startingPose) {
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, false);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, isBlue, emptyStorage);
        intake = new Intake(hm, telemetry);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hm, telemetry, startingPose);
        determineGoalPos(isBlue);
    }

    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage) {
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, false);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, isBlue, emptyStorage);
        driveTrain = new DriveTrain(hm, telemetry);
        intake = new Intake(hm, telemetry);
        determineGoalPos(isBlue);
    }

    // ===========================
    // Helper classes / methods
    // ===========================
    private static class ShotInfo {
        double range;
        double shotDirX;
        double shotDirY;
        double robotVelAlongShot;
    }

    private ShotInfo computeHybridTarget(Pose odomPose, Vector odomVel) {
        ShotInfo info = new ShotInfo();
        double correctedX, correctedY;

        if (cameraSwivel.locked) {
            // Use camera-provided robot-relative coordinates
            correctedX = cameraSwivel.x + odomPose.getX();
            correctedY = cameraSwivel.y + odomPose.getY();
        } else {
            // Fallback to pure odometry
            correctedX = goalFieldX - odomPose.getX();
            correctedY = goalFieldY - odomPose.getY();
        }

        info.range = Math.sqrt(correctedX * correctedX + correctedY * correctedY);
        info.shotDirX = correctedX / info.range;
        info.shotDirY = correctedY / info.range;

        // velocity along shot vector
        info.robotVelAlongShot = odomVel.getXComponent() * info.shotDirX + odomVel.getYComponent() * info.shotDirY;

        // update camera swivel PID target
        updateCameraPID(info);
        return info;
    }
    private void updateCameraPID(ShotInfo info){
        cameraSwivel.setPIDTarget(CameraDetectionConfig.TICKS_PER_DEGREE * Math.atan2(info.shotDirX, info.shotDirY));
    }

    private void updateSubsystems(boolean queueEmpty, double range, double robotVelAlongShot) {
        shooter.dynamicUpdate(queueEmpty, range, robotVelAlongShot);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        cameraSwivel.update();
    }

    private void handleQueueButtons(Gamepad gamepad) {
        if (gamepad.squareWasPressed()) storage.setQueue(Scoring.convertToScoringPattern(cameraSwivel.motif));
        if (gamepad.triangleWasPressed()) storage.setQueue(Scoring.G);
        if (gamepad.crossWasPressed()) storage.setQueue(Scoring.P);
        if (gamepad.circleWasPressed()) storage.setQueue(Scoring.NONE);
    }

    // ===========================
    // TeleOp update
    // ===========================
    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        Pose odomPose = pedroFollowerDriveTrain.follower.getPose();
        Vector odomVel = pedroFollowerDriveTrain.follower.getVelocity();

        ShotInfo shot = computeHybridTarget(odomPose, odomVel);
        updateSubsystems(storage.queueIsEmpty(), shot.range, shot.robotVelAlongShot);

        // allow manual camera override
        cameraSwivel.update(gamepad2.left_stick_x);

        // update drive
        pedroFollowerDriveTrain.update(gamepad1);

        // handle gamepad queue buttons
        handleQueueButtons(gamepad2);
    }

    // ===========================
    // Simple TeleOp update
    // ===========================
    public void simpleUpdate(Gamepad gamepad1, Gamepad gamepad2) {
        double range = Math.sqrt(cameraSwivel.x * cameraSwivel.x + cameraSwivel.y * cameraSwivel.y);

        cameraSwivel.update(gamepad2.left_stick_x);
        shooter.simpleUpdate(storage.queueIsEmpty(), range);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        driveTrain.update(gamepad1, gamepad1.right_bumper);

        handleQueueButtons(gamepad2);
    }

    // ===========================
    // Autonomous updates
    // ===========================
    public void autoUpdate(Follower follower) {
        Pose odomPose = follower.getPose();
        Vector odomVel = follower.getVelocity();

        ShotInfo shot = computeHybridTarget(odomPose, odomVel);
        updateSubsystems(storage.queueIsEmpty(), shot.range, shot.robotVelAlongShot);
    }

    public void autoSimpleUpdate() {
        double range = Math.sqrt(cameraSwivel.x * cameraSwivel.x + cameraSwivel.y * cameraSwivel.y);
        cameraSwivel.update();
        shooter.simpleUpdate(storage.queueIsEmpty(), range);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
    }
}
