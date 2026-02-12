package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name = "TEST: No Camera Test", group = "TEST")
public class NoCamTest extends OpMode {
    PIDStorage storage;
    Intake intake;
    Shooter shooter;
    DriveTrain driveTrain;

    @Override
    public void init() {
        storage = new PIDStorage(hardwareMap, telemetry, true, true);
        intake = new Intake(hardwareMap,telemetry);
        shooter = new Shooter(hardwareMap, telemetry);
        driveTrain = new DriveTrain(hardwareMap, telemetry);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop() {
        shooter.simpleUpdate(storage.queueIsEmpty(), gamepad1.left_stick_y * 2000);
        storage.update(shooter.isWarmedUp());
        intake.update(storage.isEmpty());

        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
        if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
        if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
        if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }
        telemetry.update();
    }
}
