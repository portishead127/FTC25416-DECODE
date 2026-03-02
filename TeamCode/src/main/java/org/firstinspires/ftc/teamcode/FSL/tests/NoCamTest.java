package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name = "TEST: No Camera", group = "TEST")
public class NoCamTest extends OpMode {
    Storage storage;
    Intake intake;
    PedroFollowerDriveTrain driveTrain;

    @Override
    public void init() {
        storage = new Storage(hardwareMap, telemetry, true);
        intake = new Intake(hardwareMap,telemetry);
        driveTrain = new PedroFollowerDriveTrain(hardwareMap, telemetry, null);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        driveTrain.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        storage.update(true);
        intake.update(storage.isEmpty());
        driveTrain.update(gamepad1);
        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }

        telemetry.update();
    }
}
