package org.firstinspires.ftc.teamcode.FSL.tests;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;

@TeleOp(name = "TEST: Turret and Camera Localization Test", group = "TEST")
public class TurretCameraLocalizationTest extends OpMode {
    Turret turret;
    PedroFollowerDriveTrain pedroFollowerDriveTrain;
    double goalFieldX;
    double goalFieldY;
    public void determineGoalPos(boolean isBlue){
        if(isBlue){
            goalFieldX = 5;
        }
        else{
            goalFieldX = 135;
        }
        goalFieldY = 140;
    }

    @Override
    public void init() {
        turret = new Turret(hardwareMap, telemetry);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hardwareMap,telemetry, new Pose(56, 8, Math.toRadians(90)));
        determineGoalPos(true);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void start() {
        pedroFollowerDriveTrain.follower.startTeleOpDrive();
    }

    @Override
    public void loop(){
        pedroFollowerDriveTrain.update(gamepad1);
        Pose odomPose = pedroFollowerDriveTrain.follower.getPose();
        calculateAndSetTurretPIDTarget(computeHybridTarget(odomPose),odomPose);
        telemetry.addData("X",odomPose.getPose().getX());
        telemetry.addData("Y",odomPose.getPose().getY());
        telemetry.update();
    }

    private Robot.ShotInfo computeHybridTarget(Pose odomPose) {
        Robot.ShotInfo info = new Robot.ShotInfo();

        double dx;
        double dy;

        dx = goalFieldX - odomPose.getX();
        dy = goalFieldY - odomPose.getY();

        info.range = Math.hypot(dx, dy);

        if (info.range > 1e-6) {
            info.shotDirX = dx / info.range;
            info.shotDirY = dy / info.range;
        } else {
            info.shotDirX = 0;
            info.shotDirY = 0;
        }
        return info;
    }
    private void calculateAndSetTurretPIDTarget(Robot.ShotInfo info, Pose odomPose) {
        double fieldAngle = Math.atan2(info.shotDirY, info.shotDirX);
        double robotAngle = fieldAngle + odomPose.getHeading();

        turret.pidController.setTarget(TurretConfig.TICKS_PER_RADIAN * robotAngle);
        turret.update();
    }
}
