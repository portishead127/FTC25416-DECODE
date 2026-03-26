package org.firstinspires.ftc.teamcode.FSL.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FSL.subsystems.Shooter;

@TeleOp(name= "TEST: Shooter PID Test", group = "TEST")
public class ShooterPIDTest extends OpMode {
    FtcDashboard f;
    Shooter shooter;
    ElapsedTime timer;
    double target = 0;
    @Override
    public void init() {
        timer = new ElapsedTime();
        f = FtcDashboard.getInstance();
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()) target = 1000;
        if(gamepad1.crossWasPressed()) target = 2000;
        if(gamepad1.triangleWasPressed()) target = 500;
        if(gamepad1.circleWasPressed()) target =0;
        shooter.fire(target);
        shooter.sendTelemetry();


        telemetry.addData("LOOP TIME", timer.milliseconds());
        telemetry.update();
        timer.reset();
    }
}
