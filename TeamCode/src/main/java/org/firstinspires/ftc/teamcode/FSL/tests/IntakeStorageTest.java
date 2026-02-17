package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;

@TeleOp(name = "TEST: Intake and Storage Test", group = "TEST")
public class IntakeStorageTest extends OpMode {
    PIDStorage storage;
    Intake intake;

    @Override
    public void init() {
        storage = new PIDStorage(hardwareMap, telemetry, true, true);
        intake = new Intake(hardwareMap,telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        storage.update(gamepad1.right_bumper);
        intake.update(storage.isEmpty());

        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
        if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
        if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
        if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }

        telemetry.update();
    }
}
