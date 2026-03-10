package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.drivetrains.Mecanum;
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
    }

    public void update(Gamepad gp){
        follower.setTeleOpDrive(
                -gp.left_stick_y,
                gp.left_stick_x,
                gp.right_stick_x,
                true // Robot Centric
        );
        follower.update();
    }

    public void sendTelemetry(){
        telemetry.addLine("PEDRO\n");
        telemetry.addData("POSE", follower.getPose());
        telemetry.addData("X VELOCITY", follower.getVelocity().getXComponent());
        telemetry.addData("Y VELOCITY", follower.getVelocity().getYComponent());
    }
}
