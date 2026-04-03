package org.firstinspires.ftc.teamcode.FSL;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;

public class Robot {
    public Motif motif = Motif.PPG;

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
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, false);
        intake = new Intake(hm, telemetry);
        turret = new Turret(hm, telemetry);
        driveTrain = new DriveTrain(hm, telemetry);
        camera = new Camera();

        determineGoalPos(isBlue);
    }

    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage, Pose startingPose) {
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, true);
        intake = new Intake(hm, telemetry);
        driveTrain = new DriveTrain(hm, telemetry);
        turret = new Turret(hm, telemetry);
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
    private void updateSubsystems(boolean queueEmpty, double range, double robotVelAlongShot) {
        shooter.dynamicUpdate(queueEmpty, range, robotVelAlongShot);
        storage.update(shooter.isWarmedUp());
    }
    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        if(gamepad1.dpadLeftWasPressed()){
            motif = Motif.GPP;
        }
        if(gamepad1.dpadUpWasPressed()){
            motif = Motif.PGP;
        }
        if(gamepad1.dpadRightWasPressed()){
            motif = Motif.PPG;
        }



        // ====================== SCORING / SHOOTER COMMANDS ======================
        if (gamepad2.triangleWasPressed()) {
            storage.setQueue(Scoring.convertToScoringPattern(motif));
            shooter.fire(1700);
            shooter.setServo(0.92);
        }if (gamepad2.squareWasPressed()) {
            storage.setQueue(Scoring.convertToScoringPattern(motif));
            shooter.fire(1900);
            shooter.setServo(0.9);
        }
        if (gamepad2.crossWasPressed()) {
            storage.setQueue(Scoring.convertToScoringPattern(motif));
            shooter.fire(2100);
            shooter.setServo(0.85);
        }

        if (gamepad2.circleWasPressed()) {
            storage.setQueue(Scoring.NONE);
            shooter.fire(0);
            // optionally: shooter.setServo(whatever your idle position is);
        }

        if (gamepad2.dpadRightWasPressed()) {
            storage.setQueue(Scoring.G);
        }
        if (gamepad2.dpadLeftWasPressed()) {
            storage.setQueue(Scoring.P);
        }

        // ====================== STORAGE & INTAKE ======================
        boolean manualOverrideActive = gamepad2.ps;   // held behavior

        if (manualOverrideActive) {
            storage.manualOverride(gamepad2.right_stick_x);   // your improved version from before
            shooter.fire(0);
        } else {
            // Normal autonomous storage control
            storage.update(gamepad2.dpadUpWasPressed());
        }

        // Intake logic
        if (gamepad2.right_trigger_pressed) {
            if (storage.isEmpty()) {
                intake.runForwards();
            }else{
                intake.stop();
            }
        } else {
            if (!storage.isEmpty()) {
                intake.runBackwards();   // reverse to help transfer
            } else {
                intake.stop();
            }
        }

        // ====================== OTHER SUBSYSTEMS ======================
        turret.update();
    }
    public void autoUpdate(Follower follower) {
    }
}
