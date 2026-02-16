package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.MecanumConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PedroFollowerDriveTrain {
    Telemetry telemetry;
    public final Follower follower;
    public PedroFollowerDriveTrain(HardwareMap hm, Telemetry telemetry, Pose startingPose) {
        this.telemetry = telemetry;
        follower = Constants.createFollower(hm);
        follower.setStartingPose(startingPose == null ? new Pose(0,0, Math.toRadians(90)) : startingPose);
        follower.update();
    }

    public void update(Gamepad gp){
        follower.update();
        if (gp.right_bumper) follower.setTeleOpDrive(
                -gp.left_stick_y* MecanumConfig.MECANUM_FULL_POWER,
                -gp.left_stick_x* MecanumConfig.MECANUM_FULL_POWER,
                -gp.right_stick_x* MecanumConfig.MECANUM_FULL_POWER,
                true // Robot Centric
        );
        else follower.setTeleOpDrive(
                -gp.left_stick_y * MecanumConfig.MECANUM_SLOW_POWER,
                -gp.left_stick_x * MecanumConfig.MECANUM_SLOW_POWER,
                -gp.right_stick_x * MecanumConfig.MECANUM_SLOW_POWER,
                true // Robot Centric
        );
        sendTelemetry();
    }

    public void sendTelemetry(){
        telemetry.addLine("PEDRO\n");
        telemetry.addData("X VELOCITY", follower.getVelocity().getXComponent());
        telemetry.addData("Y VELOCITY", follower.getVelocity().getYComponent());
        telemetry.addData("POSE", follower.getPose());
    }
}
