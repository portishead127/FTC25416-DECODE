package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
@TeleOp(name = "TEST: Shooter Test", group = "TEST")
public class ShooterTest extends OpMode {
    double target;
    double servoPos;
    Shooter shooter;
    double targetStep;
    double servoStep;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, telemetry);
        target = 0;
        servoPos = 0.5;
        targetStep = 50;
        servoStep = 0.1;
        telemetry.addLine("OPTIMISE ANGLE FOR LOWEST RPM NEEDED.");
        telemetry.addLine("USE CALCULATOR AND PEN AND PAPER TO GET COEFFICIENTS FOR REGRESSIONS SEEN BELOW:");
        telemetry.addLine("SERVOPOS = f(RANGE), = a*RANGE + b");
        telemetry.addLine("MOTOR VEL = g(RANGE), = a*RANGE^2 + b*RANGE + c");
        telemetry.addLine("ALSO COLLECT FLIGHT TIMES.\n");
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()){ shooter.pidController.setTarget(target); }
        if(gamepad1.crossWasPressed()){ shooter.pidController.setTarget(0); }

        if(gamepad1.rightBumperWasPressed()){ target += targetStep; }
        if(gamepad1.leftBumperWasPressed()){ target -= targetStep; }

        if(gamepad1.triangleWasPressed()){ shooter.setServo(servoPos); };

        if(gamepad1.dpadUpWasPressed()){ servoPos += servoStep; }
        if(gamepad1.dpadDownWasPressed()){ servoPos -= servoStep; }

        if(gamepad1.dpadRightWasPressed()){ servoStep += 0.01; }
        if(gamepad1.dpadLeftWasPressed()){ servoStep -= 0.01; }

        if(gamepad1.psWasPressed()){ targetStep += 50; }
        if(gamepad1.touchpadWasPressed()){ targetStep -= 50; }

        shooter.motor.setPower(shooter.pidController.calculate(shooter.motor.getVelocity()));
        shooter.setServo(servoPos);

        telemetry.addLine("TEST VARIABLES\n");
        telemetry.addData("VARIABLE POWER SCALAR", target);
        telemetry.addData("VARIABLE SERVO POS", servoPos);
        telemetry.addData("TARGET STEP", targetStep);
        telemetry.addData("SERVO STEP", servoStep);

        telemetry.update();
    }
}
