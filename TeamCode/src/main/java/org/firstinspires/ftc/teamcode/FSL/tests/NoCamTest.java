package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Shooter;

import java.util.List;
@Disabled
@TeleOp(name = "TEST: No Camera", group = "TEST")
public class NoCamTest extends OpMode {
    Storage storage;
    Intake intake;
    Shooter shooter;
    PedroFollowerDriveTrain driveTrain;
    ElapsedTime timer;

    @Override
    public void init() {
        timer = new ElapsedTime();
        shooter = new Shooter(hardwareMap, telemetry);
        storage = new Storage(hardwareMap, telemetry, true);
        intake = new Intake(hardwareMap,telemetry);
        driveTrain = new PedroFollowerDriveTrain(hardwareMap, telemetry, null);

        List<LynxModule> hubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void start() {
        driveTrain.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        shooter.staticUpdate(storage.queueIsEmpty(), 50);
        storage.update(gamepad1.dpadUpWasPressed());
        driveTrain.update(gamepad1);

        storage.sendTelemetry();

        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
        if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
        if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
        if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }

        telemetry.addData("LOOP TIME", timer.milliseconds());
        telemetry.update();

        timer.reset();
    }
}
