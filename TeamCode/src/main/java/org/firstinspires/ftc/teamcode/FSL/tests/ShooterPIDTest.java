package org.firstinspires.ftc.teamcode.FSL.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

@TeleOp(name= "TEST: Shooter PID Test", group = "TEST")
public class ShooterPIDTest extends OpMode {
    FtcDashboard f;
    Shooter shooter;
    @Override
    public void init() {
        f = FtcDashboard.getInstance();
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()) shooter.pidController.setTarget(1000);
        if(gamepad1.crossWasPressed()) shooter.pidController.setTarget(2000);
        if(gamepad1.triangleWasPressed()) shooter.pidController.setTarget(500);
        if(gamepad1.circleWasPressed()) shooter.pidController.setTarget(0);
        shooter.fire();
        telemetry.update();


        TelemetryPacket packet = new TelemetryPacket();
        packet.put("POS", shooter.motor.getCurrentPosition());
        packet.put("TARGET", shooter.pidController.getTarget());
        f.sendTelemetryPacket(packet);
    }
}
