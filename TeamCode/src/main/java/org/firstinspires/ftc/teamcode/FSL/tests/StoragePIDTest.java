package org.firstinspires.ftc.teamcode.FSL.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Storage;

@TeleOp(name= "TEST: Storage PID Test", group = "TEST")
public class StoragePIDTest extends OpMode {
    FtcDashboard f;
    Storage storage;
    Intake intake;
    @Override
    public void init() {
        f = FtcDashboard.getInstance();
        storage = new Storage(hardwareMap, telemetry, true);
        intake = new Intake(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        storage.update(true);
        if(gamepad1.squareWasPressed()){storage.goToSlot0AlignedWithIntake();}
        if(gamepad1.circleWasPressed()){storage.goToSlot0AlignedWithShooter();}
        if(gamepad1.leftBumperWasPressed()){storage.rotate1Slot(true);}
        if(gamepad1.rightBumperWasPressed()){storage.rotate1Slot(false);}
        telemetry.update();


        TelemetryPacket packet = new TelemetryPacket();
        packet.put("POS", storage.motor.getCurrentPosition());
        packet.put("TARGET", storage.pidController.getTarget());
        f.sendTelemetryPacket(packet);
    }
}
