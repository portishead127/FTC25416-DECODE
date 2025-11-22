package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

import java.util.LinkedList;

@TeleOp(name= "Test: Storage Test", group = "Test")
public class StorageTest extends LinearOpMode {
    Storage storage = new Storage(hardwareMap, telemetry);
    LinkedList<Color> P = new LinkedList<Color>();
    LinkedList<Color> G = new LinkedList<Color>();
    LinkedList<Color> PPG = new LinkedList<Color>();
    @Override
    public void runOpMode() throws InterruptedException {
        P.add(Color.PURPLE);
        G.add(Color.GREEN);

        PPG.add(Color.PURPLE);
        PPG.add(Color.PURPLE);
        PPG.add(Color.GREEN);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.right_bumper) { storage.spin(); }
            else{ storage.stop(); }
            if (gamepad1.squareWasPressed()) { storage.setQueue(PPG); }
            if (gamepad1.crossWasPressed()) { storage.setQueue(P); }
            if (gamepad1.circleWasPressed()) { storage.setQueue(G); }
            storage.spinThroughQueue();
        }
    }
}
