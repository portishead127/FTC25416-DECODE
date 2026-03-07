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
    Shooter shooter;
    PedroFollowerDriveTrain driveTrain;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, telemetry);
        storage = new Storage(hardwareMap, telemetry, true);
        intake = new Intake(hardwareMap,telemetry);
        driveTrain = new PedroFollowerDriveTrain(hardwareMap, telemetry, null);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void start() {
        driveTrain.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        shooter.dynamicUpdate(storage.queueIsEmpty(), 50, 0);
        storage.update(shooter.isWarmedUp() || gamepad1.dpadUpWasPressed());
        intake.update(storage.isEmpty());
        driveTrain.update(gamepad1);

        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
        if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
        if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
        if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }

        telemetry.update();
    }
}
