package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
@TeleOp(name = "TEST: Shooter Test", group = "TEST")
public class ShooterTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Shooter shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.squareWasPressed()){ shooter.fire(0.9); }
            if(gamepad1.crossWasPressed()){ shooter.fire(0.75); }
            if(gamepad1.circleWasPressed()){ shooter.stop(); }
            if(gamepad1.dpadUpWasPressed()){ shooter.addToServo(); }
            if(gamepad1.dpadDownWasPressed()){ shooter.subtractFromServo(); }

            shooter.sendTelemetry();
            telemetry.update();
        }
    }
}
