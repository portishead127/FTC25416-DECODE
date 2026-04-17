package org.firstinspires.ftc.teamcode.FSL.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.control.Localization;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.GoalPose;
import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Disabled
@TeleOp(name = "TEST: Turret Localization Test", group = "TEST")
public class TurretLocalizationTest extends OpMode {
    Turret turret;
    Follower follower;
    DriveTrain driveTrain;
    @Override
    public void init() {
        turret = new Turret(hardwareMap, telemetry);
        follower = Constants.createFollower(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap, telemetry);

        Localization.setDesiredGoalPose(GoalPose.blueGoal);
        follower.setStartingPose(new Pose(25,8, Math.toRadians(90)));

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }
    @Override
    public void loop(){
        Pose robotPose = follower.getPose();

        turret.setTargetAsRad(Localization.calculateTurretAngle(robotPose));
        driveTrain.setDriveCoefficients(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("ROBOT X", robotPose.getX());
        telemetry.addData("ROBOT Y", robotPose.getY());

        double xToGoal = GoalPose.blueGoal.getX() - robotPose.getX();
        double yToGoal = GoalPose.blueGoal.getY() - robotPose.getY();

        telemetry.addData("X TO GOAL", xToGoal);
        telemetry.addData("Y TO GOAL", yToGoal);

        telemetry.addData("FIELD ANGLE", Math.toDegrees(Localization.calculateFieldCentricAngle(robotPose)));
        telemetry.addData("ROBOT ANGLE", Math.toDegrees(robotPose.getHeading()));
        telemetry.addData("TURRET ANGLE", Math.toDegrees(Localization.calculateTurretAngle(robotPose)));

        follower.update();
        turret.update();
        driveTrain.update();
        telemetry.update();
    }
}
