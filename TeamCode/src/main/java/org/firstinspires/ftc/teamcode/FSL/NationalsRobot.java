package org.firstinspires.ftc.teamcode.FSL;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.control.Localization;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.GoalPose;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsIntake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsShooter;
import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class NationalsRobot {
    private final Turret turret;
    private final ServoStorage storage;
    private final NationalsShooter shooter;
    private final NationalsIntake intake;
    private final DriveTrain driveTrain;
    private final Follower follower;
    private final Camera camera;
    private boolean intakeRequested;

    public NationalsRobot(HardwareMap hm, Telemetry telemetry, boolean empty, boolean isBlue) {
        shooter = new NationalsShooter(hm, telemetry);
        storage = new ServoStorage(hm, telemetry, empty, shooter);
        intake = new NationalsIntake(hm);
        driveTrain = new DriveTrain(hm, telemetry);
        turret = new Turret(hm, telemetry);
        camera = new Camera();
        follower = Constants.createFollower(hm);

        if(isBlue) Localization.setDesiredGoalPose(GoalPose.blueGoal);
        else Localization.setDesiredGoalPose(GoalPose.redGoal);

        intakeRequested = false;
    }
    public void setStartingPose(Pose pose){
        follower.setStartingPose(pose);
    }
    public void readMotif(){
        camera.readMotif();
    }
    public NationalsRobot(HardwareMap hm, Telemetry telemetry) {
        this(hm, telemetry, true, true);
    }
    public void update() {
        turret.setTargetAsRad(Localization.calculateTurretAngle(follower.getPose()));
        handleIntake();
        handleShooter();

        turret.update();
        shooter.update();
        intake.update();
        storage.update();
        driveTrain.update();
        follower.update();
    }
    private void handleShooter() {
        if (storage.isIntaking()) {
            shooter.stop();
        } else {
            shooter.prepareForShot(Localization.calculateDistance(follower.getPose()));
        }
    }
    public void toggleFaceForward(){
        turret.toggleFaceForward();
    }
    public void setIntakeRequested(boolean value) {
        intakeRequested = value;
    }
    public void setDriveTrain(double x, double y, double rx, boolean slow, boolean fast){
        driveTrain.setDriveCoefficients(x,y,rx);
        driveTrain.setFast(fast && !slow);
        driveTrain.setSlow(slow && !fast);
        driveTrain.setMed(!slow && !fast);
    }
    private void switchToShootingMode() {
        if (storage.isIntaking()) {
            storage.forceShoot();
        }
    }
    public void fireGreen() {
        switchToShootingMode();
        storage.setQueue(Color.GREEN);
    }
    public void firePurple() {
        switchToShootingMode();
        storage.setQueue(Color.PURPLE);
    }
    public void fireMotif() {
        switchToShootingMode();
        storage.setQueue(Scoring.convertToScoringPattern(camera.getMotif()));
    }
    public void fireAny() {
        switchToShootingMode();
        storage.allowAny();
    }
    public void forceIntake(){
        if(!storage.isIntaking()){
            storage.forceIntake();
        }
    }
    private void handleIntake() {
        if (storage.isIntaking()) {
            if (intakeRequested) {
                intake.runIntake();
            } else {
                intake.stop();
            }
        }
        else{
            if (intakeRequested){
                intake.stop();
            } else {
                intake.runTransfer();
            }
        }
    }
    public boolean isIntaking(){
        return storage.isIntaking();
    }
    public Follower getFollower() {
        return follower;
    }
    public boolean isFollowerBusy() {
        return follower.isBusy();
    }
    public void followPath(PathChain path) {
        follower.followPath(path);
    }
    public void followPath(PathChain path, boolean holdEnd) {
        follower.followPath(path, holdEnd);
    }
    public Pose getPose() {
        return follower.getPose();
    }
}
