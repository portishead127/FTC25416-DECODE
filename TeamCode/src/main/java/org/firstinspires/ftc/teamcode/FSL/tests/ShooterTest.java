package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
@TeleOp(name = "TEST: Shooter Test", group = "TEST")
public class ShooterTest extends OpMode {
    double powerScalar;
    double servoPos;
    Shooter shooter;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, telemetry);
        powerScalar = 0;
        servoPos = 0.5;
        telemetry.addLine("OPTIMISE ANGLE FOR LOWEST RPM NEEDED.");
        telemetry.addLine("USE CALCULATOR AND PEN AND PAPER TO GET COEFFICIENTS FOR REGRESSIONS SEEN BELOW:");
        telemetry.addLine("ANGLE = f(RANGE), = a*RANGE + b");
        telemetry.addLine("MOTOR VEL = g(RANGE), = a*RANGE^2 + b*RANGE + c");
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()){ shooter.fire(powerScalar); }
        if(gamepad1.crossWasPressed()){ shooter.stop(); }

        if(gamepad1.rightBumperWasPressed()){ powerScalar += 0.02; }
        if(gamepad1.leftBumperWasPressed()){ powerScalar -= 0.02; }

        if(gamepad1.triangleWasPressed()){ shooter.setServo(servoPos); };

        if(gamepad1.dpadUpWasPressed()){ servoPos += 0.02; }
        if(gamepad1.dpadDownWasPressed()){ servoPos -= 0.02; }

        telemetry.addLine("TEST VARIABLES\n");
        telemetry.addData("VARIABLE POWER SCALAR", powerScalar);
        telemetry.addData("VARIABLE SERVO POS", servoPos);

        telemetry.update();
    }
}
