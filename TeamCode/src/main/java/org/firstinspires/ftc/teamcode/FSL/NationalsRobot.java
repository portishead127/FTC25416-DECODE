package org.firstinspires.ftc.teamcode.FSL;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.control.Localization;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.GoalPose;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsIntake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsShooter;
import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class NationalsRobot {
//    private final Turret turret;
//    private final ServoStorage storage;
    private final ElapsedTime shootTimer;
    private final NationalsShooter shooter;
    private final NationalsIntake intake;
    private final DriveTrain driveTrain;
    private final Follower follower;
//    private final Camera camera;
    public NationalsRobot(HardwareMap hm, Telemetry telemetry, boolean empty, boolean isBlue) {
        shooter = new NationalsShooter(hm, telemetry);
//        storage = new ServoStorage(hm, telemetry, empty, shooter);
        intake = new NationalsIntake(hm);
        driveTrain = new DriveTrain(hm, telemetry);
//        turret = new Turret(hm, telemetry);
//        camera = new Camera();
        follower = Constants.createFollower(hm);

        if(isBlue) Localization.setDesiredGoalPose(GoalPose.blueGoal);
        else Localization.setDesiredGoalPose(GoalPose.redGoal);

        shootTimer = new ElapsedTime();
    }
//    public void setControllers(Gamepad gamepad1, Gamepad gamepad2){
//        storage.setControllers(gamepad1, gamepad2);
//    }
    public void setStartingPose(Pose pose){
        if(pose == null){
            pose = new Pose(0,0, Math.toRadians(90));
        }
        follower.setStartingPose(pose);
    }
//    public void readMotif(){
//        camera.readMotif();
//    }
//    public void setMotif(Motif motif){
//        camera.setMotif(motif);
//    }
    public NationalsRobot(HardwareMap hm, Telemetry telemetry) {
        this(hm, telemetry, true, true);
    }
    public void update() {
//        turret.setTargetAsRad(Localization.calculateTurretAngle(follower.getPose()));
//        handleIntake();
//        handleShooter();
        if(shooter.isShooterReady()) intake.runTransfer();

        shooter.update();
        intake.update();
        driveTrain.update();
        follower.update();
    }
    public void autoUpdate(){
        if(shooter.isShooterReady()) intake.runTransfer();

        shooter.autoUpdate();
        intake.update();
        driveTrain.update();
        follower.update();
        shooter.sendTelemetry();
    }
    public void runJustIntake(){
        intake.runJustIntake();
    }
    public void emergencyAuto(){
        driveTrain.auto();
    }
//    private void handleShooter() {
//        if (storage.isIntaking()) {
//            shooter.stop();
//        } else {
//            shooter.prepareForShot(Localization.calculateDistance(follower.getPose()));
//        }//        robot.setIntakeRequested(gamepad2.right_trigger_pressed);

//    }
//    public void toggleFaceForward(){
//        turret.toggleFaceForward();
//    }
//    public void setIntakeRequested(boolean value) {
//        intakeRequested = value;
//    }
    public void setDriveTrain(double x, double y, double rx, boolean slow, boolean fast){
        driveTrain.setDriveCoefficients(x,y,rx);
        driveTrain.setFast(fast && !slow);
        driveTrain.setSlow(slow && !fast);
        driveTrain.setMed(!slow && !fast);
    }
    public void faceGoal(){
        double error = Localization.calculateFieldCentricAngle(getPose()) - getPose().getHeading();
        setDriveTrain(0,0, -1 * error, false, false);
    }
    public void setBlockerOpen(){
        shooter.setBlockerOpen();
    }
    public void SIMPLEprepareForShot(ShotPos shotPos){
        shooter.prepareForShot(shotPos);
        shootTimer.reset();
    }
    public void SIMPLEintake(){
        shooter.stop();
        intake.runIntake();
    }
    public void SIMPLEintakestop(){
        intake.stop();
    }
    public void SIMPLEstopShooter(){
        shooter.stop();
    }
    public boolean finishedShooting(){
        return shootTimer.milliseconds() > 2000;
    }
//    private void switchToShootingMode() {
//        if (storage.isIntaking()) {
//            storage.forceShoot();
//        }
////    }
//    public void fireGreen() {
//        switchToShootingMode();
//        storage.setQueue(Color.GREEN);
//    }
//    public void firePurple() {
//        switchToShootingMode();
//        storage.setQueue(Color.PURPLE);
//    }
//    public void fireMotif() {
//        switchToShootingMode();
//        storage.setQueue(Scoring.convertToScoringPattern(camera.getMotif()));
//    }
//    public void fireAny() {
//        switchToShootingMode();
//        storage.allowAny();
//    }
//    public void forceIntake(){
//        if(!storage.isIntaking()){
//            storage.forceIntake();
//        }
//    }
//    private void handleIntake() {
//        if (storage.isIntaking()) {
//            if (intakeRequested) {
//                intake.runIntake();
//            } else {
//                intake.stop();
//            }
//        }
//        else{
//            if (intakeRequested){
//                intake.stop();
//            } else {
//                intake.runTransfer();
//            }
//        }
//    }
//    public boolean isIntaking(){
//        return storage.isIntaking();
//    }
    public Follower getFollower() {
        return follower;
    }
    public boolean isFollowerBusy() {
        return follower.isBusy();
    }
    public void followPath(PathChain path, boolean holdEnd) {
        follower.followPath(path, holdEnd);
    }
    public void followPath(PathChain path, boolean holdEnd, double maxPower) {
        follower.followPath(path, maxPower, holdEnd);
    }
    public void followPath(Path path){
        follower.followPath(path);
    }
    public Pose getPose() {
        return follower.getPose();
    }
}
