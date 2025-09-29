package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Limelight3ADetectionConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
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

    public Command fire = new SetPower(shooterMotors,1);
    public Command fireHalf = new SetPower(shooterMotors, 0.5);
    public Command stop = new SetPower(shooterMotors, 0);
}