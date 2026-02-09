package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;

@TeleOp(name= "TEST: Storage Test", group = "TEST")
public class StorageTest extends OpMode {

    PIDStorage storage;
    @Override
    public void init() {
        storage = new PIDStorage(hardwareMap, telemetry, true);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        storage.update(true);
        if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
        if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
        if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
        if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }
        telemetry.update();
    }
}
