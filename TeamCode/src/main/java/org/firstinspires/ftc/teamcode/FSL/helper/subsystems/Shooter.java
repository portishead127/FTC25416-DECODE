package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {
    //min 0.3
    //max 0.7
    private double servoPos = 0.5;
    private double motorPower = 0;
    public static Shooter INSTANCE = new Shooter();
    private Shooter(){}
    private final MotorGroup shooterMotors = new MotorGroup(
            new MotorEx("SM1")
                    .reversed()
                    .brakeMode(),
            new MotorEx("SM2")
                    .reversed()
                    .brakeMode()
    );

    @Override
    public void initialize() {
        servo.setPosition(0.3);
    }

    public final ServoEx servo = new ServoEx("SHS");
    public Command fire = new SetPower(shooterMotors,1).requires(this);
    public Command fireHalf = new SetPower(shooterMotors, 0.5).requires(this);
    public Command stop = new SetPower(shooterMotors, 0).requires(this);
    public Command fireAtPower = new LambdaCommand().setUpdate(() -> shooterMotors.setPower(motorPower)).requires(this);
    public Command updateServo = new LambdaCommand()
            .setUpdate(() -> servo.setPosition(servoPos))
            .requires(this);
    public Command addToServo = new InstantCommand(() -> servoPos += 0.05).requires(this);
    public Command subtractFromServo = new InstantCommand(() -> servoPos -= 0.05).requires(this);
    public Command addToPower = new InstantCommand(() -> motorPower += 0.1).requires(this);
    public Command subtractFromPower = new InstantCommand(() -> motorPower -= 0.1).requires(this);
    public Command telemetryServo = new LambdaCommand()
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addData("MOTOR POWER", shooterMotors.getPower());
                ActiveOpMode.telemetry().addData("SHOOTER SERVO", servo.getPosition());
                ActiveOpMode.telemetry().addData("DESIRED MOTOR POWER", motorPower);
            }).requires(this);
}