package org.firstinspires.ftc.teamcode.FSL.tests;

import com.bylazar.gamepad.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.EdgeDetector;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
@TeleOp(name = "Test: Shooter Test", group = "Test")
public class ShooterTest extends LinearOpMode {
    Shooter shooter = new Shooter(hardwareMap, telemetry);
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.squareWasPressed()){ shooter.fire(); }
            if(gamepad1.crossWasPressed()){ shooter.fireHalf(); }
            if(gamepad1.circleWasPressed()){ shooter.stop(); }
            if(gamepad1.dpadUpWasPressed()){ shooter.addToServo(); }
            if(gamepad1.dpadDownWasPressed()){ shooter.substractFromServo(); }

            shooter.telemetryServo();
            telemetry.update();
        }
    }
}
