package org.firstinspires.ftc.teamcode.FSL.helper;

import android.graphics.pdf.content.PdfPageGotoLinkContent;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
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
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, false);
        intake = new Intake(hm, telemetry);
        turret = new Turret(hm, telemetry);

        determineGoalPos(isBlue);
    }

    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean emptyStorage, Pose startingPose) {
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry, true);
        intake = new Intake(hm, telemetry);
        driveTrain = new DriveTrain(hm, telemetry);
        turret = new Turret(hm, telemetry);
        turret.pidController.setTarget(0);
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
        if (gamepad2.triangleWasPressed()) {

            //TODODODOODOD




            storage.setQueue(Scoring.convertToScoringPattern(Motif.PPG));
            shooter.fire(1900);
            shooter.setServo(0.9);









        }
        if (gamepad2.squareWasPressed()) {
            storage.setQueue(Scoring.convertToScoringPattern(Motif.PPG));
            shooter.fire(1900);
            shooter.setServo(0.9);
        }
        if(gamepad2.crossWasPressed()){
            storage.setQueue(Scoring.convertToScoringPattern(Motif.PPG));
            shooter.fire(2100);
            shooter.setServo(0.85);
        }

        if(gamepad2.circleWasPressed()){
            storage.setQueue(Scoring.NONE);
            shooter.fire(0);
        }
        storage.update(gamepad2.dpadUpWasPressed());
        if(gamepad2.right_trigger_pressed){
            if(storage.isEmpty()){
                intake.runForwards();
            }
        }
        else{
            if(!storage.isEmpty()){
                intake.runBackwards();
            }
            else{
                intake.stop();
            }
        }

        if(gamepad2.dpadRightWasPressed()){
            storage.setQueue(Scoring.G);
        }

        if(gamepad2.dpadLeftWasPressed()){
            storage.setQueue(Scoring.P);
        }
        turret.update();
        driveTrain.update(gamepad1, gamepad1.right_trigger_pressed, gamepad1.left_trigger_pressed);
    }
    public void autoUpdate(Follower follower) {
    }
}
