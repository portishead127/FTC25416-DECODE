package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

import java.util.LinkedList;

@TeleOp(name= "Test: Storage Test", group = "Test")
public class StorageTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Storage storage = new Storage(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.right_bumper) { storage.spin(); }
            else{ storage.spinThroughQueue(); }
            if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
            if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
            if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
            if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }

            storage.sendTelemetry();
            telemetry.update();
        }
    }
}
