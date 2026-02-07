package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
@TeleOp(name = "TEST: Shooter Test", group = "TEST")
public class ShooterTest extends OpMode {

    Shooter shooter;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()){ shooter.fire(0.9); }
        if(gamepad1.crossWasPressed()){ shooter.fire(0.75); }
        if(gamepad1.circleWasPressed()){ shooter.stop(); }
        if(gamepad1.dpadUpWasPressed()){ shooter.addToServo(); }
        if(gamepad1.dpadDownWasPressed()){ shooter.subtractFromServo(); }

        telemetry.update();
    }
}
