package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {
    public static Shooter INSTANCE = new Shooter();
    private Shooter(){}
    private final MotorEx motor1 = new MotorEx("SM1")
            .reversed()
            .brakeMode();
    private final MotorEx motor2 = new MotorEx("SM2")
            .reversed()
            .brakeMode();

    public Command fire = new ParallelGroup(
            new SetPower(motor1,1),
            new SetPower(motor2, 1)
    );
    public Command fireHalf = new ParallelGroup(
            new SetPower(motor1,0.5),
            new SetPower(motor2, 0.5)
    );
    public Command stop = new ParallelGroup(
            new SetPower(motor1,0),
            new SetPower(motor2, 0)
    );
}
