package org.firstinspires.ftc.teamcode.FSL.tests;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Turret;

@TeleOp(name = "TEST: Turret and Camera Localization Test", group = "TEST")
public class TurretCameraLocalizationTest extends OpMode {
    Turret turret;
    Camera camera;
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
        camera = new Camera(hardwareMap,telemetry, true);
        pedroFollowerDriveTrain = new PedroFollowerDriveTrain(hardwareMap,telemetry, new Pose(56, 8, Math.toRadians(90)));
        determineGoalPos(true);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        pedroFollowerDriveTrain.update(gamepad1);
        calculateAndSetTurretPIDTarget(computeHybridTarget(pedroFollowerDriveTrain.follower.getPose(), pedroFollowerDriveTrain.follower.getVelocity()), pedroFollowerDriveTrain.follower.getPose());
        camera.update();
        telemetry.update();
    }

    private Robot.ShotInfo computeHybridTarget(Pose odomPose, Vector odomVel) {
        Robot.ShotInfo info = new Robot.ShotInfo();

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
    private void calculateAndSetTurretPIDTarget(Robot.ShotInfo info, Pose odomPose) {
        double fieldAngle = Math.atan2(info.shotDirY, info.shotDirX);
        double robotAngle = fieldAngle - odomPose.getHeading();

        turret.pidController.setTarget(TurretConfig.TICKS_PER_RADIAN * robotAngle);
        turret.update();
    }
}
