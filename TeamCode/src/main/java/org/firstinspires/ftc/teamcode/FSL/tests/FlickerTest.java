package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.Storage;

@TeleOp(name = "TEST: Flicker Test", group = "TEST")
public class FlickerTest extends OpMode {
    Storage storage;
    @Override
    public void init() {
        storage = new Storage(hardwareMap, telemetry, true);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop() {
        if(gamepad1.crossWasPressed()) storage.startFlick();
        if(gamepad1.squareWasPressed()) storage.servo.setPosition(0.5);
        if(gamepad1.circleWasPressed()) storage.servo.setPosition(0.4);
        storage.updateFlick();
        telemetry.update();
        storage.sendTelemetry();
    }
}
