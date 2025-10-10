package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.driving.HolonomicMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

public class MecanumSet implements Subsystem {
    private final MotorGroup mecanumSetMotors = new MotorGroup(
            new MotorEx("FLW")
                    .brakeMode(),
            new MotorEx("FRW")
                    .brakeMode()
                    .reversed(),
            new MotorEx("BLW")
                    .brakeMode(),
            new MotorEx("BRW")
                    .brakeMode()
                    .reversed()
    );
    private MecanumSet(){}

    @Override
    public void initialize() {
        setMedium.schedule();
    }

    public static final MecanumSet INSTANCE = new MecanumSet();
    public final MecanumDriverControlled driverController = new MecanumDriverControlled(
            mecanumSetMotors.getLeader(),
            mecanumSetMotors.getFollowers()[0],
            mecanumSetMotors.getFollowers()[1],
            mecanumSetMotors.getFollowers()[2],
            Gamepads.gamepad1().leftStickY(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX()
    );

    public Command setSlow = new InstantCommand(() -> driverController.setScalar(0.4)).requires(this);
    public Command setMedium = new InstantCommand(() -> driverController.setScalar(0.7)).requires(this);
    public Command setFast = new InstantCommand(() -> driverController.setScalar(1)).requires(this);
}
