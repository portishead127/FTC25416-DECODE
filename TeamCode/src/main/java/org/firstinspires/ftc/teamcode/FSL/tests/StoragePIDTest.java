package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;

@TeleOp(name= "TEST: Storage Test", group = "TEST")
public class StoragePIDTest extends OpMode {

    PIDStorage storage;
    @Override
    public void init() {
        storage = new PIDStorage(hardwareMap, telemetry, true, true);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        storage.update(true);
        if(gamepad1.squareWasPressed()){storage.goToSlot1AlignedWithIntake();}
        if(gamepad1.circleWasPressed()){storage.goToSlot1AlignedWithShooter();}
        if(gamepad1.leftBumperWasPressed()){storage.rotate1Slot(true);}
        if(gamepad1.rightBumperWasPressed()){storage.rotate1Slot(false);}
        telemetry.update();
    }
}
