package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name= "Test: Storage and Shooter Test", group = "Test")
public class StorageShooterTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        PIDStorage storage = new PIDStorage(hardwareMap, telemetry);
        Shooter shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            storage.update(shooter.isWarmedUp());
            shooter.update(storage.queueIsEmpty(), 0);

            if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
            if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
            if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
            if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }

            storage.sendTelemetry();
            telemetry.update();
        }
    }
}
