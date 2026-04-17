package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Storage;
@Disabled
@TeleOp(name = "TEST: Flicker Test", group = "TEST")
public class FlickerTest extends OpMode {

    ServoStorage storage;
    @Override
    public void init() {
        storage = new ServoStorage(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop() {
        if(gamepad1.cross) storage.spinFlicker();
        else storage.stopFlicker();
        telemetry.update();
    }
}
