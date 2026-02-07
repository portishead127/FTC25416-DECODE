package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

@TeleOp(name = "TEST: No Camera", group = "TEST")
public class NoCamTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        PIDStorage storage = new PIDStorage(hardwareMap, telemetry, true);
        Intake intake = new Intake(hardwareMap,telemetry);
        Shooter shooter = new Shooter(hardwareMap, telemetry);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            shooter.update(storage.queueIsEmpty(), gamepad1.left_stick_y * 2000);
            storage.update(shooter.isWarmedUp());
            intake.run(!storage.isFull());

            if (gamepad1.squareWasPressed()) { storage.setQueue(Scoring.PPG); }
            if (gamepad1.crossWasPressed()) { storage.setQueue(Scoring.P); }
            if (gamepad1.circleWasPressed()) { storage.setQueue(Scoring.G); }
            if (gamepad1.triangleWasPressed()) { storage.setQueue(Scoring.NONE); }
        }
    }
}
