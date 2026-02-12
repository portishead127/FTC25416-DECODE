package org.firstinspires.ftc.teamcode.FSL.tests;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.configs.MecanumConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "TEST: Pedro Mecanum Test", group = "TEST")
public class PedroMecanumTest extends OpMode {
    private Follower follower;
    public static Pose startingPose; //See ExampleAuto to understand how to use this
    private boolean slowMode = false;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
    }

    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        //Call this once per loop
        follower.update();
        sendTelemetry();

        if (!slowMode) follower.setTeleOpDrive(
                -gamepad1.left_stick_y* MecanumConfig.MECANUM_FULL_POWER,
                -gamepad1.left_stick_x* MecanumConfig.MECANUM_FULL_POWER,
                -gamepad1.right_stick_x* MecanumConfig.MECANUM_FULL_POWER,
                true // Robot Centric
        );
        else follower.setTeleOpDrive(
                -gamepad1.left_stick_y * MecanumConfig.MECANUM_SLOW_POWER,
                -gamepad1.left_stick_x * MecanumConfig.MECANUM_SLOW_POWER,
                -gamepad1.right_stick_x * MecanumConfig.MECANUM_SLOW_POWER,
                true // Robot Centric
        );
        //Slow Mode
        if (gamepad1.right_bumper) {slowMode = !slowMode;}
    }

    public void sendTelemetry(){
        telemetry.addLine("PEDRO\n");
        telemetry.addData("X VELOCITY", follower.getVelocity().getXComponent());
        telemetry.addData("Y VELOCITY", follower.getVelocity().getYComponent());
        telemetry.addData("POSE", follower.getPose());
    }
}

