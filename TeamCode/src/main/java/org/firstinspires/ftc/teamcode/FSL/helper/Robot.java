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

public class Robot{
    public CameraSwivel cameraSwivel;
    public Shooter shooter;
    public PIDStorage storage;
    public DriveTrain driveTrain;
    public Intake intake;
    public PedroFollowerDriveTrain pedroFollowerDriveTrain;
    public static final double goalFieldX = 144;
    public static final double goalFieldY = 72;
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isGreedyAuto, boolean emptyStorage, Follower autoFollower){
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, isGreedyAuto);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, isBlue, emptyStorage);
        driveTrain = new DriveTrain(hm, telemetry);
        intake = new Intake(hm, telemetry);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hm, telemetry, autoFollower);
    }
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage, Pose startingPose){
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, false);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, isBlue, emptyStorage);
        driveTrain = new DriveTrain(hm, telemetry);
        intake = new Intake(hm, telemetry);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hm, telemetry, startingPose);
    }

    public void simpleUpdate(Gamepad gamepad1, Gamepad gamepad2) {
        cameraSwivel.update(gamepad2.left_stick_x);
        shooter.simpleUpdate(storage.queueIsEmpty(), Math.sqrt(cameraSwivel.x * cameraSwivel.x + cameraSwivel.y * cameraSwivel.y));
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        driveTrain.update(gamepad1, gamepad1.right_bumper);

        if(gamepad2.squareWasPressed()){ storage.setQueue(Scoring.convertToScoringPattern(cameraSwivel.motif)); }
        if(gamepad2.triangleWasPressed()){ storage.setQueue(Scoring.G); }
        if(gamepad2.crossWasPressed()){ storage.setQueue(Scoring.P); }
        if(gamepad2.circleWasPressed()){ storage.setQueue(Scoring.NONE); }
    }

    public void autoSimpleUpdate() {
        cameraSwivel.update();
        shooter.simpleUpdate(storage.queueIsEmpty(), Math.sqrt(cameraSwivel.x * cameraSwivel.x + cameraSwivel.y * cameraSwivel.y));
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
    }

    public void autoUpdate(Follower follower) {
        // 1️⃣ Get odometry
        Pose odomPose = follower.getPose();          // x, y, heading
        Vector odomVel = follower.getVelocity();       // vx, vy in field coordinates

        double range;
        double shotDirX, shotDirY;

        // 2️⃣ Hybrid camera + odometry
        if(cameraSwivel.locked){
            // Camera provides robot-relative goal coordinates
            double correctedX = cameraSwivel.x + odomPose.getX();
            double correctedY = cameraSwivel.y + odomPose.getY();
            range = Math.sqrt(correctedX * correctedX + correctedY * correctedY);

            shotDirX = correctedX / range;
            shotDirY = correctedY / range;
        } else {
            // Fallback to odometry only
            double dx = goalFieldX - odomPose.getX();
            double dy = goalFieldY - odomPose.getY();
            range = Math.sqrt(dx * dx + dy * dy);

            shotDirX = dx / range;
            shotDirY = dy / range;
        }

        // 3️⃣ Compute robot velocity along shot
        double robotVelocityAlongShot = odomVel.getXComponent() * shotDirX + odomVel.getYComponent() * shotDirY;

        shooter.dynamicUpdate(storage.queueIsEmpty(), range, robotVelocityAlongShot);
        cameraSwivel.setPIDTarget(CameraDetectionConfig.TICKS_PER_DEGREE * Math.atan2(shotDirY, shotDirX));
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        cameraSwivel.update();
    }

    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        // 1️⃣ Get odometry
        Pose odomPose = pedroFollowerDriveTrain.follower.getPose();          // x, y, heading
        Vector odomVel = pedroFollowerDriveTrain.follower.getVelocity();       // vx, vy in field coordinates

        double range;
        double shotDirX, shotDirY;

        // 2️⃣ Hybrid camera + odometry
        if(cameraSwivel.locked){
            // Camera provides robot-relative goal coordinates
            double correctedX = cameraSwivel.x + odomPose.getX();
            double correctedY = cameraSwivel.y + odomPose.getY();
            range = Math.sqrt(correctedX * correctedX + correctedY * correctedY);

            shotDirX = correctedX / range;
            shotDirY = correctedY / range;
        } else {
            // Fallback to odometry only
            double dx = goalFieldX - odomPose.getX();
            double dy = goalFieldY - odomPose.getY();
            range = Math.sqrt(dx * dx + dy * dy);

            shotDirX = dx / range;
            shotDirY = dy / range;
        }

        // 3️⃣ Compute robot velocity along shot
        double robotVelocityAlongShot = odomVel.getXComponent() * shotDirX + odomVel.getYComponent() * shotDirY;

        shooter.dynamicUpdate(storage.queueIsEmpty(), range, robotVelocityAlongShot);
        cameraSwivel.setPIDTarget(CameraDetectionConfig.TICKS_PER_DEGREE * Math.atan2(shotDirY, shotDirX));
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(storage.isEmpty());
        cameraSwivel.update();
        pedroFollowerDriveTrain.update(gamepad1);

        if(gamepad2.squareWasPressed()){ storage.setQueue(Scoring.convertToScoringPattern(cameraSwivel.motif)); }
        if(gamepad2.triangleWasPressed()){ storage.setQueue(Scoring.G); }
        if(gamepad2.crossWasPressed()){ storage.setQueue(Scoring.P); }
        if(gamepad2.circleWasPressed()){ storage.setQueue(Scoring.NONE); }
    }
}
