package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import dev.nextftc.core.commands.Command;
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

    public final ServoEx servo = new ServoEx("SHS");

    public Command fire = new SetPower(shooterMotors,1);
    public Command fireHalf = new SetPower(shooterMotors, 0.5);
    public Command stop = new SetPower(shooterMotors, 0);
    public Command addToServo = new SetPosition(servo, servo.getPosition() + 0.01);
    public Command subtractFromServo = new SetPosition(servo, servo.getPosition() - 0.01);
    public Command telemetryServo = new LambdaCommand()
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addData("Servo", servo.getPosition());
            }).perpetually();
}